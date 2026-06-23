package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record FlightRequest(
        @NotBlank(message = "Flight number is required") String flightNumber,
        @NotNull(message = "Departure time is required") @Future(message = "Departure time must be in the future") Instant departureTime,
        @NotNull(message = "Arrival time is required") @Future(message = "Arrival time must be in the future") Instant arrivalTime,
        @NotBlank(message = "Source is required") String source,
        @NotBlank(message = "Destination is required") String destination,
        @NotNull(message = "Aircraft id is required") Integer aircraftId
) {
}

