package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.CouponStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CouponDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    BranchDto branch,
    String code,
    ClientDto client,
    OffsetDateTime expiredAt,
    CouponStatus status
) {
}
