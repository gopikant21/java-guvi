package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByBookingBookingId(Long bookingId);
    Optional<Payment> findByBookingBookingId(Long bookingId);
}

