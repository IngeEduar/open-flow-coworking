package com.nelumbo.open_flow_coworking.model.response.coupon;

import com.nelumbo.open_flow_coworking.model.response.client.ClientResponse;
import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;

import java.time.OffsetDateTime;

public record CouponResponse(
        OffsetDateTime createdAt,
        String code,
        ClientResponse client,
        OffsetDateTime expiredAt,
        CouponStatus status
) {
}
