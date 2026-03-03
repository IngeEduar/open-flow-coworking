package com.nelumbo.open_flow_coworking.shared.dto;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto (
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Boolean recycle,
    String name,
    String email,
    String document,
    String password,
    String salt,
    UserRole role
) {
}
