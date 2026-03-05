package com.nelumbo.open_flow_coworking.model.request.client;

import jakarta.validation.constraints.NotBlank;

public record ClientDocumentRequest(
        @NotBlank(message = "El documento es obligatorio")
        String document
) {
}
