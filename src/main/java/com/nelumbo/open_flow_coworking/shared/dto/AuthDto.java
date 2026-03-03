package com.nelumbo.open_flow_coworking.shared.dto;

public record AuthDto (
    String token,
    String refreshToken,
    long expiresIn,
    String email,
    String password
) {
}
