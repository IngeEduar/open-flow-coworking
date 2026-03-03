package com.nelumbo.open_flow_coworking.shared.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthDto {
    private String token;
    private String refreshToken;
    private long expiresIn;
    private String email;
    private String password;
}
