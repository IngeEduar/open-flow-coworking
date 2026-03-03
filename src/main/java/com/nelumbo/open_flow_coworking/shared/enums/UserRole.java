package com.nelumbo.open_flow_coworking.shared.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("Administrador"),
    OPERATOR("Operador");

    private final String code;

    UserRole(String code) {
        this.code = code;
    }
}
