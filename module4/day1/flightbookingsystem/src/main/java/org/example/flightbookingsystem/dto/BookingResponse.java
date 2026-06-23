package org.example.flightbookingsystem.dto;

import org.example.flightbookingsystem.model.BookingStatus;

import java.time.Instant;

public record BookingResponse(
        Long bookingId,
        Instant bookingDate,
        BookingStatus bookingStatus,
        double totalAmount,
        Integer passengerId,
        String passengerName,
        Integer flightId,
        String flightNumber
) {
}

