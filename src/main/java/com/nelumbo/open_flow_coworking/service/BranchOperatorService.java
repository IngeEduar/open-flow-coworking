package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.BranchOperatorDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BranchOperatorService {
    Page<BranchOperatorDto> listBranchOperators(UUID branchId,  int limit, int page);
    Page<BranchOperatorDto> listOperatorBranches(String token, int limit, int page);
    BranchOperatorDto addOperatorToBranch(UUID userId, UUID branchId);
    void deleteOperatorToBranch(UUID userId, UUID branchId);
}
