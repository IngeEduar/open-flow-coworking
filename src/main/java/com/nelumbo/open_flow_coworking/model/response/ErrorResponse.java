package com.nelumbo.open_flow_coworking.model.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse (
    String error,
    HttpStatus status,
    int internalCode
) {
}
