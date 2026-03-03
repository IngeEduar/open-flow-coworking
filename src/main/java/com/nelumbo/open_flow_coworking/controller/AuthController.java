package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.AuthMapper;
import com.nelumbo.open_flow_coworking.mappers.UserMapper;
import com.nelumbo.open_flow_coworking.model.request.authentication.LoginRequest;
import com.nelumbo.open_flow_coworking.model.response.authentication.AuthResponse;
import com.nelumbo.open_flow_coworking.model.response.user.UserDetailResponse;
import com.nelumbo.open_flow_coworking.service.AuthService;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import com.nelumbo.open_flow_coworking.shared.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserDetailResponse> getProfile() {
        UserDto userDto = authService.getProfile();

        return ResponseEntity.ok(userMapper.toDetailResponse(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest body
    ) {
        AuthDto bodyDto = authMapper.toDto(body);
        AuthDto response = authService.login(bodyDto);

        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-Refresh-Token") String refreshHeader
    ) {
        AuthDto response = authService.refreshToken(authHeader, refreshHeader);

        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
