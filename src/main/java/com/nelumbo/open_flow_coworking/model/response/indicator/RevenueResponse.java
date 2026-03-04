package com.nelumbo.open_flow_coworking.model.response.indicator;

import java.math.BigDecimal;

public record RevenueResponse(
        BigDecimal revenueToday,
        BigDecimal revenueThisWeek,
        BigDecimal revenueThisMonth,
        BigDecimal revenueThisYear
) {
}
