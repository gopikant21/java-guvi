package org.example.practicetest.service;

import org.example.practicetest.entity.Shipment;

import java.util.List;

public interface ShipmentService {
    List<Shipment> getAll();

    Shipment getById(Long id);
}

