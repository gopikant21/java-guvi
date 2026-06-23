package org.example.flightbookingsystem.controller;

import jakarta.validation.Valid;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.dto.PassengerRequest;
import org.example.flightbookingsystem.dto.PassengerResponse;
import org.example.flightbookingsystem.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponse> create(@Valid @RequestBody PassengerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(passengerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<PassengerResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(passengerService.getAll(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> update(@PathVariable Integer id, @Valid @RequestBody PassengerRequest request) {
        return ResponseEntity.ok(passengerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

