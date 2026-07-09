package org.example.practicetest.service;

import org.example.practicetest.entity.Shipment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoopShipmentService implements ShipmentService {
    @Override
    public List<Shipment> getAll() {
        return List.of();
    }

    @Override
    public Shipment getById(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

