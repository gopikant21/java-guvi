package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.dto.PassengerRequest;
import org.example.flightbookingsystem.dto.PassengerResponse;
import org.example.flightbookingsystem.exceptions.BusinessException;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Passenger;
import org.example.flightbookingsystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    public PassengerResponse create(PassengerRequest request) {
        validateUniqueFields(request, null);
        Passenger passenger = new Passenger();
        apply(passenger, request);
        return toResponse(passengerRepository.save(passenger));
    }

    @Transactional(readOnly = true)
    public PassengerResponse getById(Integer id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<PassengerResponse> getAll(int page, int size) {
        Page<PassengerResponse> result = passengerRepository
                .findAll(PageRequest.of(page, size, Sort.by("passengerId").descending()))
                .map(this::toResponse);
        return new PagedResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    public PassengerResponse update(Integer id, PassengerRequest request) {
        Passenger passenger = getEntity(id);
        validateUniqueFields(request, id);
        apply(passenger, request);
        return toResponse(passengerRepository.save(passenger));
    }

    public void delete(Integer id) {
        passengerRepository.delete(getEntity(id));
    }

    Passenger getEntity(Integer id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + id));
    }

    private void validateUniqueFields(PassengerRequest request, Integer existingId) {
        passengerRepository.findByEmail(request.email().trim()).ifPresent(existing -> {
            if (existingId == null || existing.getPassengerId() != existingId) {
                throw new BusinessException("Passenger email already exists: " + request.email());
            }
        });

        passengerRepository.findByPassportNumber(request.passportNumber().trim()).ifPresent(existing -> {
            if (existingId == null || existing.getPassengerId() != existingId) {
                throw new BusinessException("Passport number already exists: " + request.passportNumber());
            }
        });
    }

    private void apply(Passenger passenger, PassengerRequest request) {
        passenger.setName(request.name().trim());
        passenger.setEmail(request.email().trim().toLowerCase());
        passenger.setPhone(request.phone());
        passenger.setPassportNumber(request.passportNumber().trim().toUpperCase());
    }

    private PassengerResponse toResponse(Passenger passenger) {
        return new PassengerResponse(
                passenger.getPassengerId(),
                passenger.getName(),
                passenger.getEmail(),
                passenger.getPhone(),
                passenger.getPassportNumber()
        );
    }
}


