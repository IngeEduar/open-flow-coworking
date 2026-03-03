package com.nelumbo.open_flow_coworking.shared.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchOperatorDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    BranchDto branch,
    UserDto user
) {
}
