package com.nelumbo.open_flow_coworking.mappers;

import com.nelumbo.open_flow_coworking.entity.Branch;
import com.nelumbo.open_flow_coworking.model.request.branch.BranchCreateRequest;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    Branch toEntity(BranchDto branchDto);
    BranchDto toDto(Branch branch);
    BranchDto toDto(BranchCreateRequest branchCreateRequest);
    BranchListResponse toListResponse(BranchDto branchDto);
    BranchDetailResponse toDetailResponse(BranchDto branchDto);
}
