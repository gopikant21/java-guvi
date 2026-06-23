package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Optional<Flight> findByFlightNumber(String flightNumber);
    Page<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination, Pageable pageable);
}

