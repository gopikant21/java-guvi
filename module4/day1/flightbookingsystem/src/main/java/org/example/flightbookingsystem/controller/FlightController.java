package org.example.flightbookingsystem.controller;

import jakarta.validation.Valid;
import org.example.flightbookingsystem.dto.FlightRequest;
import org.example.flightbookingsystem.dto.FlightResponse;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(flightService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<FlightResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination
    ) {
        return ResponseEntity.ok(flightService.getAll(page, size, source, destination));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponse> update(@PathVariable Integer id, @Valid @RequestBody FlightRequest request) {
        return ResponseEntity.ok(flightService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        flightService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

