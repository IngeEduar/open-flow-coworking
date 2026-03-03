package com.nelumbo.open_flow_coworking.shared.enums;

import lombok.Getter;

@Getter
public enum AccessStatus {
    ACTIVE("Activo"),
    COMPLETED("Completado");

    private final String code;

    AccessStatus(String code) {
        this.code = code;
    }
}
