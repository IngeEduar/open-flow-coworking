package com.nelumbo.open_flow_coworking.shared.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BranchOperatorDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private BranchDto branch;
    private UserDto user;
}
