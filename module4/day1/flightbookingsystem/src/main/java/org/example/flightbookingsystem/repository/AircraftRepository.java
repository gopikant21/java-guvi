package org.example.flightbookingsystem.repository;

import org.example.flightbookingsystem.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Integer> {
}

