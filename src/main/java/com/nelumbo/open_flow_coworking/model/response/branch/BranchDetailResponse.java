package com.nelumbo.open_flow_coworking.model.response.branch;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchDetailResponse(
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
