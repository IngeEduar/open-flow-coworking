package com.nelumbo.open_flow_coworking.model.response.authentication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private long expiresIn;
}
