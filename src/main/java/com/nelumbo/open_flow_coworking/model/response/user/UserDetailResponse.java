package com.nelumbo.open_flow_coworking.model.response.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDetailResponse (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    String name,
    String email,
    String document,
    UserRole role
) {
}
