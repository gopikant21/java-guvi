package org.example.flightbookingsystem.service;

import org.example.flightbookingsystem.dto.AircraftRequest;
import org.example.flightbookingsystem.dto.AircraftResponse;
import org.example.flightbookingsystem.dto.PagedResponse;
import org.example.flightbookingsystem.exceptions.ResourceNotFoundException;
import org.example.flightbookingsystem.model.Aircraft;
import org.example.flightbookingsystem.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AircraftService {

    @Autowired
    private AircraftRepository aircraftRepository;

    public AircraftResponse create(AircraftRequest request) {
        Aircraft aircraft = new Aircraft();
        aircraft.setModel(request.model().trim());
        aircraft.setCapacity(request.capacity());
        return toResponse(aircraftRepository.save(aircraft));
    }

    @Transactional(readOnly = true)
    public AircraftResponse getById(Integer id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public PagedResponse<AircraftResponse> getAll(int page, int size) {
        Page<AircraftResponse> result = aircraftRepository
                .findAll(PageRequest.of(page, size, Sort.by("aircraftId").descending()))
                .map(this::toResponse);
        return toPagedResponse(result);
    }

    public AircraftResponse update(Integer id, AircraftRequest request) {
        Aircraft aircraft = getEntity(id);
        aircraft.setModel(request.model().trim());
        aircraft.setCapacity(request.capacity());
        return toResponse(aircraftRepository.save(aircraft));
    }

    public void delete(Integer id) {
        Aircraft aircraft = getEntity(id);
        aircraftRepository.delete(aircraft);
    }

    Aircraft getEntity(Integer id) {
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + id));
    }

    private AircraftResponse toResponse(Aircraft aircraft) {
        return new AircraftResponse(aircraft.getAircraftId(), aircraft.getModel(), aircraft.getCapacity());
    }

    private PagedResponse<AircraftResponse> toPagedResponse(Page<AircraftResponse> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}


