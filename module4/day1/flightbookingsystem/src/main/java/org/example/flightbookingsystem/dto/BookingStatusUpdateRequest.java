package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import org.example.flightbookingsystem.model.BookingStatus;

public record BookingStatusUpdateRequest(
        @NotNull(message = "Booking status is required") BookingStatus bookingStatus
) {
}

