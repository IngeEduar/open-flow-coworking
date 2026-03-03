package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccessLogDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    ClientDto client,
    BranchDto branch,
    UserDto operator,
    OffsetDateTime checkIn,
    OffsetDateTime checkOut,
    BigDecimal price,
    AccessStatus status
) {
}
