package org.example.flightbookingsystem.dto;

import org.example.flightbookingsystem.model.CabinClass;

public record TicketResponse(
        Integer ticketId,
        String seatNumber,
        CabinClass cabinClass,
        Long bookingId,
        Integer flightId,
        String flightNumber
) {
}

