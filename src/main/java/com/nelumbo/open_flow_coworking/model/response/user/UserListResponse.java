package com.nelumbo.open_flow_coworking.model.response.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

import java.util.UUID;

public record UserListResponse (
    UUID id,
    String name,
    String email,
    String document,
    UserRole role
) {
}
