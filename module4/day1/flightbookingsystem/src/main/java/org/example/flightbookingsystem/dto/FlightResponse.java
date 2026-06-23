package org.example.flightbookingsystem.dto;

import java.time.Instant;

public record FlightResponse(
        Integer flightId,
        String flightNumber,
        Instant departureTime,
        Instant arrivalTime,
        String source,
        String destination,
        Integer aircraftId,
        String aircraftModel
) {
}

