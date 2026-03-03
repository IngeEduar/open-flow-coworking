package com.nelumbo.open_flow_coworking.model.response.authentication;

public record AuthResponse (
     String token,
     String refreshToken,
     long expiresIn
) {
}
