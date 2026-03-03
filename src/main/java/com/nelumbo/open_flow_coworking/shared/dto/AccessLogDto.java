package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class AccessLogDto {
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Boolean recycle;
    private ClientDto client;
    private BranchDto branch;
    private UserDto operator;
    private OffsetDateTime checkIn;
    private OffsetDateTime checkOut;
    private BigDecimal price;
    private AccessStatus status;
}
