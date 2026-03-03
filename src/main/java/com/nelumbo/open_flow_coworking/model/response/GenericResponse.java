package com.nelumbo.open_flow_coworking.model.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse {
    private String message;
    private HttpStatus status;
}
