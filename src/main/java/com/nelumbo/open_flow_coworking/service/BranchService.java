package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BranchService {
    Page<BranchDto> searchBranch(String q, int limit, int page);
    BranchDto branchDetail(UUID branchId);
    BranchDto createBranch(BranchDto branchDto);
    BranchDto updateBranch(UUID branchId, BranchDto branchDto);
    void deleteBranch(UUID branchId);
}
