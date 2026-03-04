package com.nelumbo.open_flow_coworking.model.response.accessLog;

import com.nelumbo.open_flow_coworking.model.response.branch.BranchDetailResponse;
import com.nelumbo.open_flow_coworking.model.response.client.ClientResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserListResponse;
import com.nelumbo.open_flow_coworking.shared.enums.AccessStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccessLogResponse(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    ClientResponse client,
    BranchDetailResponse branch,
    UserListResponse operator,
    OffsetDateTime checkIn,
    OffsetDateTime checkOut,
    BigDecimal price,
    AccessStatus status
) {
}
