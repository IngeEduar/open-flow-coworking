package com.nelumbo.open_flow_coworking.model.request.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

public record UserCreateRequest (
    String name,
    String email,
    String document,
    UserRole role,
    String password
) {
}
