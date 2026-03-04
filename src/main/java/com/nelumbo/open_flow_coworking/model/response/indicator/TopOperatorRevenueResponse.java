package com.nelumbo.open_flow_coworking.model.response.indicator;

import java.math.BigDecimal;

public record TopOperatorRevenueResponse(
        String name,
        BigDecimal totalRevenue
) {
}
