package com.nelumbo.open_flow_coworking.model.request.user;

import com.nelumbo.open_flow_coworking.shared.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest (
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    String email,

    @NotBlank(message = "El documento es obligatorio")
    String document,

    @NotNull(message = "El rol es obligatorio")
    UserRole role,

    @NotBlank(message = "La contraseña es obligatoria")
    String password
) {
}
