package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.entity.AccessLog;
import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.entity.Client;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.mappers.AccessLogMapper;
import com.nelumbo.open_flow_coworking.repository.*;
import com.nelumbo.open_flow_coworking.service.AccessLogService;
import com.nelumbo.open_flow_coworking.shared.dto.AccessLogDto;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;
import com.nelumbo.open_flow_coworking.util.CreatePageable;
import com.nelumbo.open_flow_coworking.model.event.CheckoutCompletedEvent;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessLogServiceImpl implements AccessLogService {

    private final BranchRepository branchRepository;
    private final BranchOperatorRepository branchOperatorRepository;
    private final ClientRepository clientRepository;
    private final AccessLogRepository accessLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    private final AccessLogMapper accessLogMapper;

    @Override
    public Page<AccessLogDto> findActiveAccessesForBranch(UUID branchId, int limit, int page) {
        User operator = getOperator();
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        if (!branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, operator)) {
            throw new OpenFlowException(2000);
        }

        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");
        Page<AccessLog> access = accessLogRepository.findByBranchAndStatus(branch, AccessStatus.ACTIVE, pageable);

        return access.map(accessLogMapper::toDto);
    }

    @Override
    public Page<AccessLogDto> findActiveAccesses(int limit, int page) {
        Pageable pageable = CreatePageable.buildPageable(page, limit, "createdAt");
        Page<AccessLog> access = accessLogRepository.findByStatus(AccessStatus.ACTIVE, pageable);

        return access.map(accessLogMapper::toDto);
    }

    @Override
    @Transactional
    public AccessLogDto checkIn(UUID branchId, ClientDto clientDto) {
        User operator = getOperator();
        Branch branch = branchRepository.findByIdAndRecycleFalseForUpdate(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        if (!branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, operator)) {
            throw new OpenFlowException(2000);
        }

        if (accessLogRepository.countByBranchAndStatus(branch, AccessStatus.ACTIVE) >= branch.getMaxCapacity()) {
            throw new OpenFlowException(2001);
        }

        Client client = clientRepository.findByDocumentForUpdate(clientDto.document())
                .orElseGet(() -> {
                    Client newClient = createClient(clientDto);
                    return clientRepository.saveAndFlush(newClient);
                });

        if (accessLogRepository.existsByClientAndStatus(client, AccessStatus.ACTIVE)) {
            throw new OpenFlowException(2002);
        }

        AccessLog accessLog = AccessLog
                .builder()
                .client(client)
                .branch(branch)
                .operator(operator)
                .checkIn(OffsetDateTime.now())
                .checkOut(null)
                .price(null)
                .status(AccessStatus.ACTIVE)
                .build();

        accessLog = accessLogRepository.save(accessLog);

        return accessLogMapper.toDto(accessLog);
    }

    private Client createClient(ClientDto clientDto) {
        return Client
                .builder()
                .document(clientDto.document())
                .email(clientDto.email())
                .build();
    }

    @Override
    @Transactional
    public AccessLogDto checkOut(UUID branchId, String clientDocument) {
        User operator = getOperator();
        Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

        if (!branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, operator)) {
            throw new OpenFlowException(2000);
        }

        Client client = clientRepository.findByDocument(clientDocument)
                .orElseThrow(() -> new OpenFlowException(2, "Client", "Document", clientDocument));

        AccessLog accessLog = accessLogRepository
                .findByClientAndBranchAndStatus(client, branch, AccessStatus.ACTIVE)
                .orElseThrow(() -> new OpenFlowException(2003));

        OffsetDateTime checkOutTime = OffsetDateTime.now();
        accessLog.setCheckOut(checkOutTime);

        BigDecimal amountToPay = calculatePrice(accessLog.getCheckIn(), checkOutTime, accessLog.getBranch().getHourlyRate());
        accessLog.setPrice(amountToPay);

        accessLog.setStatus(AccessStatus.COMPLETED);
        accessLog = accessLogRepository.save(accessLog);

        eventPublisher.publishEvent(new CheckoutCompletedEvent(this, client.getId(), branch.getId()));

        return accessLogMapper.toDto(accessLog);
    }

    private BigDecimal calculatePrice(OffsetDateTime checkIn, OffsetDateTime checkOut, BigDecimal pricePerHour) {
        Duration duration = Duration.between(checkIn, checkOut);
        long minutes = duration.toMinutes();

        long billableHours = (long) Math.ceil(minutes / 60.0);

        if (billableHours == 0) {
            billableHours = 1;
        }

        return pricePerHour.multiply(BigDecimal.valueOf(billableHours));
    }

    private User getOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                Objects.equals(authentication.getPrincipal(), "anonymousUser")
        ) {
            throw new OpenFlowException(1002);
        }

        return (User) authentication.getPrincipal();
    }
}
