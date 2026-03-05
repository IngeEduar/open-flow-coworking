package com.nelumbo.open_flow_coworking.model.request.branch;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BranchUpdateRequest(
        @NotBlank(message = "El nombre de la sede es obligatorio")
        String name,

        @NotBlank(message = "La dirección de la sede es obligatoria")
        String address,

        @Min(value = 1, message = "La capacidad máxima debe ser de al menos 1")
        int maxCapacity,

        @NotNull(message = "La tarifa por hora es obligatoria")
        @Positive(message = "La tarifa por hora debe ser mayor a 0")
        BigDecimal hourlyRate
) {
}
