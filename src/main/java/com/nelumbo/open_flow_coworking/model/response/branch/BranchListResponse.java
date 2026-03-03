package com.nelumbo.open_flow_coworking.model.response.branch;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record BranchListResponse(
        UUID id,
        String name,
        String address,
        int maxCapacity,
        BigDecimal hourlyRate
) {
}
