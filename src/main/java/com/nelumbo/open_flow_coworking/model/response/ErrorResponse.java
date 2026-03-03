package com.nelumbo.open_flow_coworking.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String error;
    private HttpStatus status;
    private int internalCode;
}
