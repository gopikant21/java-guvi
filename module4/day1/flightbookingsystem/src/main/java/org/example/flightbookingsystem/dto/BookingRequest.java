package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.flightbookingsystem.model.BookingStatus;

public record BookingRequest(
        @NotNull(message = "Passenger id is required") Integer passengerId,
        @NotNull(message = "Flight id is required") Integer flightId,
        @PositiveOrZero(message = "Total amount cannot be negative") double totalAmount,
        BookingStatus bookingStatus
) {
}

