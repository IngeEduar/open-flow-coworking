package com.nelumbo.open_flow_coworking.service.Implementation;

import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.repository.AccessLogRepository;
import com.nelumbo.open_flow_coworking.service.IndicatorService;
import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import com.nelumbo.open_flow_coworking.shared.dto.RevenueDto;
import com.nelumbo.open_flow_coworking.shared.dto.TopClientProjection;
import com.nelumbo.open_flow_coworking.shared.dto.TopRevenueProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndicatorServiceImpl implements IndicatorService {
    private final AccessLogRepository accessLogRepository;

    private final ClientMapper clientMapper;

    public List<TopClientProjection> getTopClientsGlobal() {
        return accessLogRepository.findTopClients(PageRequest.of(0, 10));
    }

    public List<TopClientProjection> getTopClientsByBranch(UUID branchId) {
        return accessLogRepository.findTopClientsByBranch(branchId, PageRequest.of(0, 10));
    }

    public List<ClientDto> getFirstTimeClients() {
        return accessLogRepository.findFirstTimeClients().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public RevenueDto getBranchRevenue(UUID branchId) {
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
}
