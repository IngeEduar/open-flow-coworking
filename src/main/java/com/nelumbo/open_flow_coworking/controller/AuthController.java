package com.nelumbo.open_flow_coworking.controller;

import jakarta.validation.Valid;

import com.nelumbo.open_flow_coworking.mappers.AuthMapper;
import com.nelumbo.open_flow_coworking.model.request.authentication.LoginRequest;
import com.nelumbo.open_flow_coworking.model.response.authentication.AuthResponse;
import com.nelumbo.open_flow_coworking.model.response.ErrorResponse;
import com.nelumbo.open_flow_coworking.service.AuthService;
import com.nelumbo.open_flow_coworking.shared.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
@Tag(name = "Authentication | Autenticación", description = "Operations related to user session and tokens | Operaciones relacionadas con la sesión del usuario y tokens")
public class AuthController {
    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping
    @Operation(summary = "Login user | Iniciar sesión de usuario", description = "Authenticates a user and returns a token | Autentica a un usuario y devuelve un token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful login | Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "400", description = "Invalid request body | Cuerpo de solicitud inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials | No autorizado - Credenciales inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest body) {
        AuthDto response = authService.login(authMapper.toDto(body));
        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @PutMapping
    @Operation(summary = "Refresh token | Refrescar token", description = "Refreshes an expired authentication token using a refresh token | Refresca un token de autenticación expirado usando un token de actualización", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully | Token refrescado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Invalid request headers | Encabezados de solicitud inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired refresh token | No autorizado - Token de actualización inválido o expirado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-Refresh-Token") String refreshHeader
    ) {
        AuthDto response = authService.refreshToken(authHeader, refreshHeader);
        return ResponseEntity.ok(authMapper.toResponse(response));
    }

    @DeleteMapping
    @Operation(summary = "Logout user | Cerrar sesión de usuario", description = "Invalidates the user's tokens | Invalida los tokens del usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successfully logged out | Sesión cerrada exitosamente"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid token | No autorizado - Token inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> logout() {
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
