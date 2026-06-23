package org.example.flightbookingsystem.controller;

import jakarta.validation.Valid;
import org.example.flightbookingsystem.dto.AircraftRequest;
import org.example.flightbookingsystem.dto.AircraftResponse;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aircrafts")
public class AircraftController {

    @Autowired
    private AircraftService aircraftService;

    @PostMapping
    public ResponseEntity<AircraftResponse> create(@Valid @RequestBody AircraftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aircraftService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AircraftResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(aircraftService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AircraftResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(aircraftService.getAll(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponse> update(@PathVariable Integer id, @Valid @RequestBody AircraftRequest request) {
        return ResponseEntity.ok(aircraftService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aircraftService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

