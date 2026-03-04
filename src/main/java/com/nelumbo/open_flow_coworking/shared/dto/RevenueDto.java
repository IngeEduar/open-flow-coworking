package com.nelumbo.open_flow_coworking.shared.dto;

import java.math.BigDecimal;

public record RevenueDto(
        BigDecimal revenueToday,
        BigDecimal revenueThisWeek,
        BigDecimal revenueThisMonth,
        BigDecimal revenueThisYear
) {}
