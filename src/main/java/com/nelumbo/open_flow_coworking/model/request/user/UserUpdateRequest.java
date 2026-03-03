package com.nelumbo.open_flow_coworking.model.request.user;

public record UserUpdateRequest (
     String name,
     String email,
     String password
) {
}
