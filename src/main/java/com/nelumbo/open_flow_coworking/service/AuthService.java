package com.nelumbo.open_flow_coworking.service;

import com.nelumbo.open_flow_coworking.model.request.authentication.LoginRequest;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;

public interface AuthService {
    AuthDto login(AuthDto request);
    AuthDto refreshToken(String authHeader, String refreshToken);
    UserDto getProfile();
    void logout();
}
