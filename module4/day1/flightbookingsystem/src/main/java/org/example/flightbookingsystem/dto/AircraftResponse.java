package org.example.flightbookingsystem.dto;

public record AircraftResponse(
        Integer aircraftId,
        String model,
        int capacity
) {
}

