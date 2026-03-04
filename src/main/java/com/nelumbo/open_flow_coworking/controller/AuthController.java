package com.nelumbo.open_flow_coworking.controller;

import com.nelumbo.open_flow_coworking.mappers.AuthMapper;
import com.nelumbo.open_flow_coworking.model.request.authentication.LoginRequest;
import com.nelumbo.open_flow_coworking.model.response.authentication.AuthResponse;
import com.nelumbo.open_flow_coworking.service.AuthService;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest body) {
        AuthDto response = authService.login(authMapper.toDto(body));
        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @PutMapping
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-Refresh-Token") String refreshHeader
    ) {
        AuthDto response = authService.refreshToken(authHeader, refreshHeader);
        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @DeleteMapping
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
