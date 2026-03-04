package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.repository.AccessLogRepository;
import com.nelumbo.open_flow_coworking.repository.BranchOperatorRepository;
import com.nelumbo.open_flow_coworking.repository.BranchRepository;
import com.nelumbo.open_flow_coworking.service.IndicatorService;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import com.nelumbo.open_flow_coworking.shared.dto.RevenueDto;
import com.nelumbo.open_flow_coworking.shared.dto.TopClientProjection;
import com.nelumbo.open_flow_coworking.shared.dto.TopRevenueProjection;
import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import com.nelumbo.open_flow_coworking.exception.OpenFlowException;
import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.entity.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndicatorServiceImpl implements IndicatorService {
    private final AccessLogRepository accessLogRepository;
    private final BranchRepository branchRepository;
    private final BranchOperatorRepository branchOperatorRepository;

    private final ClientMapper clientMapper;

    public List<TopClientProjection> getTopClientsGlobal() {
        return accessLogRepository.findTopClients(PageRequest.of(0, 10));
    }

    public List<TopClientProjection> getTopClientsByBranch(UUID branchId) {
        validateBranchAccessIfOperator(branchId);
        return accessLogRepository.findTopClientsByBranch(branchId, PageRequest.of(0, 10));
    }

    public List<ClientDto> getFirstTimeClients() {
        return accessLogRepository.findFirstTimeClients().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public RevenueDto getBranchRevenue(UUID branchId) {
        validateBranchAccessIfOperator(branchId);

        OffsetDateTime now = OffsetDateTime.now();

        BigDecimal today = accessLogRepository.sumRevenueByBranchAndDateAfter(branchId,
                now.truncatedTo(ChronoUnit.DAYS));
        BigDecimal week = accessLogRepository.sumRevenueByBranchAndDateAfter(branchId, now.minusDays(7));
        BigDecimal month = accessLogRepository.sumRevenueByBranchAndDateAfter(branchId, now.minusMonths(1));
        BigDecimal year = accessLogRepository.sumRevenueByBranchAndDateAfter(branchId, now.minusYears(1));

        return new RevenueDto(today, week, month, year);
    }

    public List<TopRevenueProjection> getTopOperatorsWeekly() {
        return accessLogRepository.findTopOperatorsByRevenue(OffsetDateTime.now().minusDays(7), PageRequest.of(0, 3));
    }

    public List<TopRevenueProjection> getTopBranchesWeekly() {
        return accessLogRepository.findTopBranchesByRevenue(OffsetDateTime.now().minusDays(7), PageRequest.of(0, 3));
    }

    private void validateBranchAccessIfOperator(UUID branchId) {
        User user = getAuthenticatedUser();
        if (user.getRole() == UserRole.OPERATOR) {
            Branch branch = branchRepository.findByIdAndRecycleFalse(branchId)
                    .orElseThrow(() -> new OpenFlowException(2, "Branch", "ID", branchId.toString()));

            if (!branchOperatorRepository.existsByBranchAndUserAndRecycleFalse(branch, user)) {
                throw new OpenFlowException(2000);
            }
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
            throw new OpenFlowException(1002);
        }

        return (User) authentication.getPrincipal();
    }
}
