package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.ClientMapper;
import com.nelumbo.open_flow_coworking.service.IndicatorService;
import com.nelumbo.open_flow_coworking.mappers.IndicatorMapper;
import com.nelumbo.open_flow_coworking.model.response.client.ClientResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.RevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopBranchRevenueResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopClientResponse;
import com.nelumbo.open_flow_coworking.model.response.indicator.TopOperatorRevenueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/indicators")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;
    private final IndicatorMapper indicatorMapper;

    private final ClientMapper clientMapper;

    @GetMapping("/clients/top-accesses")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<TopClientResponse>> getTopClientsGlobal() {
        return ResponseEntity.ok(indicatorService.getTopClientsGlobal().stream()
                .map(indicatorMapper::toTopClientResponse).toList());
    }

    @GetMapping("/branches/{branchId}/clients/top-accesses")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<TopClientResponse>> getTopClientsByBranch(@PathVariable UUID branchId) {
        return ResponseEntity.ok(indicatorService.getTopClientsByBranch(branchId).stream()
                .map(indicatorMapper::toTopClientResponse).toList());
    }

    @GetMapping("/clients/first-time")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<ClientResponse>> getFirstTimeClients() {
        return ResponseEntity.ok(indicatorService.getFirstTimeClients().stream()
                .map(clientMapper::toResponse).toList());
    }

    @GetMapping("/branches/{branchId}/revenue")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<RevenueResponse> getBranchRevenue(@PathVariable UUID branchId) {
        return ResponseEntity.ok(indicatorMapper.toRevenueResponse(indicatorService.getBranchRevenue(branchId)));
    }

    @GetMapping("/operators/top-revenue/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopOperatorRevenueResponse>> getTopOperatorsWeekly() {
        return ResponseEntity.ok(indicatorService.getTopOperatorsWeekly().stream()
                .map(indicatorMapper::toTopOperatorRevenueResponse).toList());
    }

    @GetMapping("/branches/top-revenue/weekly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopBranchRevenueResponse>> getTopBranchesWeekly() {
        return ResponseEntity.ok(indicatorService.getTopBranchesWeekly().stream()
                .map(indicatorMapper::toTopBranchRevenueResponse).toList());
    }
}
