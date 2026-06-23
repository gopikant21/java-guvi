package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByPassengerPassengerId(Integer passengerId, Pageable pageable);
}

