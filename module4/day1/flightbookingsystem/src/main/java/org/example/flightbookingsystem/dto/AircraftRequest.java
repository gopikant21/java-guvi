package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AircraftRequest(
        @NotBlank(message = "Model is required") String model,
        @Positive(message = "Capacity must be greater than 0") int capacity
) {
}

