package com.nelumbo.open_flow_coworking.model.request.authentication;

public record LoginRequest (
    String email,
    String password
) {
}
