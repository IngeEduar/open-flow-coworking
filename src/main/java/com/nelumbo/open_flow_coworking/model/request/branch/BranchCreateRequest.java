package com.nelumbo.open_flow_coworking.model.request.branch;

import java.math.BigDecimal;

public record BranchCreateRequest(
        String name,
        String address,
        int maxCapacity,
        BigDecimal hourlyRate
) {
}
