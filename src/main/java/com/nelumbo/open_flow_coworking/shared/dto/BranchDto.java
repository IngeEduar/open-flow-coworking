package com.nelumbo.open_flow_coworking.shared.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    String name,
    String address,
    int maxCapacity,
    BigDecimal hourlyRate
) {
}
