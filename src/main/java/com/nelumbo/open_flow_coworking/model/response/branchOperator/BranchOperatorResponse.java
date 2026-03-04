package com.nelumbo.open_flow_coworking.model.response.branchOperator;

import com.nelumbo.open_flow_coworking.model.response.branch.BranchListResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.shared.dto.BranchDto;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchOperatorResponse(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    BranchListResponse branch,
    UserListResponse user
) {
}
