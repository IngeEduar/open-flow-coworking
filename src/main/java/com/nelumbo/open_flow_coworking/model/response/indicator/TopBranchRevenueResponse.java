package com.nelumbo.open_flow_coworking.model.response.indicator;

import java.math.BigDecimal;

public record TopBranchRevenueResponse(
        String name,
        BigDecimal totalRevenue
) {
}
