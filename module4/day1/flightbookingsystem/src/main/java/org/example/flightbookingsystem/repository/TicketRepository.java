package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    boolean existsBySeatNumberIgnoreCaseAndBookingFlightFlightId(String seatNumber, Integer flightId);
    Page<Ticket> findByBookingBookingId(Long bookingId, Pageable pageable);
}

