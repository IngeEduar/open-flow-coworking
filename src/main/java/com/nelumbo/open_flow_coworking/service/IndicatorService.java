package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.ClientDto;
import com.nelumbo.open_flow_coworking.shared.dto.RevenueDto;
import com.nelumbo.open_flow_coworking.shared.dto.TopClientProjection;
import com.nelumbo.open_flow_coworking.shared.dto.TopRevenueProjection;

import java.util.List;
import java.util.UUID;

public interface IndicatorService {
    List<TopClientProjection> getTopClientsGlobal();
    List<TopClientProjection> getTopClientsByBranch(UUID branchId);
    List<ClientDto> getFirstTimeClients();
    List<TopRevenueProjection> getTopOperatorsWeekly();
    List<TopRevenueProjection> getTopBranchesWeekly();
    RevenueDto getBranchRevenue(UUID branchId);
}