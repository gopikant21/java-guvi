package org.example.flightbookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.flightbookingsystem.model.CabinClass;

public record TicketRequest(
        @NotNull(message = "Booking id is required") Long bookingId,
        @NotBlank(message = "Seat number is required") String seatNumber,
        @NotNull(message = "Cabin class is required") CabinClass cabinClass
) {
}

