package com.nelumbo.open_flow_coworking.shared.enums;

import lombok.Getter;

@Getter
public enum CouponStatus {
    ACTIVE("Activo"),
    EXPIRED("Expirado"),
    USED("Usado");

    private final String code;

    CouponStatus(String code) {
        this.code = code;
    }
}
