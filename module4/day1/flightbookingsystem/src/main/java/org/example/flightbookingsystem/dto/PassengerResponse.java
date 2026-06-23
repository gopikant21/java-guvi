package org.example.flightbookingsystem.dto;

public record PassengerResponse(
        Integer passengerId,
        String name,
        String email,
        String phone,
        String passportNumber
) {
}

