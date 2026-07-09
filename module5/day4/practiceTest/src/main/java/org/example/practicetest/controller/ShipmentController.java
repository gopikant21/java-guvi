package org.example.practicetest.controller;

import org.example.practicetest.entity.Shipment;
import org.example.practicetest.service.ShipmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<Shipment> getAll() {
        return shipmentService.getAll();
    }

    @GetMapping("/{id}")
    public Shipment getById(@PathVariable Long id) {
        return shipmentService.getById(id);
    }
}

