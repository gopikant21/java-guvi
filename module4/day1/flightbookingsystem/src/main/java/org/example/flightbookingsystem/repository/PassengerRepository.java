package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Optional<Passenger> findByEmail(String email);
    Optional<Passenger> findByPassportNumber(String passportNumber);
}

