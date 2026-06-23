package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.FlightRequest;
import org.example.flightbookingsystem.dto.FlightResponse;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.exceptions.BusinessException;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Aircraft;
import org.example.flightbookingsystem.model.Flight;
import org.example.flightbookingsystem.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AircraftService aircraftService;

    public FlightResponse create(FlightRequest request) {
        validateFlightNumber(request.flightNumber(), null);
        validateTimeRange(request);
        Flight flight = new Flight();
        apply(flight, request);
        return toResponse(flightRepository.save(flight));
    }

    @Transactional(readOnly = true)
    public FlightResponse getById(Integer id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<FlightResponse> getAll(int page, int size, String source, String destination) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("departureTime").ascending());
        Page<Flight> flightPage;
        if (source != null && !source.isBlank() && destination != null && !destination.isBlank()) {
            flightPage = flightRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(source.trim(), destination.trim(), pageable);
        } else {
            flightPage = flightRepository.findAll(pageable);
        }

        Page<FlightResponse> mapped = flightPage.map(this::toResponse);
        return new PagedResponse<>(
                mapped.getContent(),
                mapped.getNumber(),
                mapped.getSize(),
                mapped.getTotalElements(),
                mapped.getTotalPages()
        );
    }

    public FlightResponse update(Integer id, FlightRequest request) {
        Flight flight = getEntity(id);
        validateFlightNumber(request.flightNumber(), id);
        validateTimeRange(request);
        apply(flight, request);
        return toResponse(flightRepository.save(flight));
    }

    public void delete(Integer id) {
        flightRepository.delete(getEntity(id));
    }

    Flight getEntity(Integer id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
    }

    private void validateFlightNumber(String flightNumber, Integer existingId) {
        flightRepository.findByFlightNumber(flightNumber.trim()).ifPresent(existing -> {
            if (existingId == null || existing.getFlightId() != existingId) {
                throw new BusinessException("Flight number already exists: " + flightNumber);
            }
        });
    }

    private void validateTimeRange(FlightRequest request) {
        if (request.arrivalTime().isBefore(request.departureTime())) {
            throw new BusinessException("Arrival time must be after departure time");
        }
    }

    private void apply(Flight flight, FlightRequest request) {
        Aircraft aircraft = aircraftService.getEntity(request.aircraftId());
        flight.setFlightNumber(request.flightNumber().trim().toUpperCase());
        flight.setDepartureTime(request.departureTime());
        flight.setArrivalTime(request.arrivalTime());
        flight.setSource(request.source().trim());
        flight.setDestination(request.destination().trim());
        flight.setAircraft(aircraft);
    }

    private FlightResponse toResponse(Flight flight) {
        return new FlightResponse(
                flight.getFlightId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getSource(),
                flight.getDestination(),
                flight.getAircraft().getAircraftId(),
                flight.getAircraft().getModel()
        );
    }
}


