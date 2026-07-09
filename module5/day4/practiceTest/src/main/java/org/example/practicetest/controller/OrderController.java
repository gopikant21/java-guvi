package org.example.practicetest.controller;

import jakarta.validation.Valid;
import org.example.practicetest.dto.order.CancelOrderRequest;
import org.example.practicetest.dto.order.ProcessOrderRequest;
import org.example.practicetest.dto.order.SplitShipmentRequest;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.Shipment;
import org.example.practicetest.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderEntity> create(@Valid @RequestBody OrderEntity order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(order));
    }

    @GetMapping
    public List<OrderEntity> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public OrderEntity getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PutMapping("/{id}")
    public OrderEntity update(@PathVariable Long id, @Valid @RequestBody OrderEntity order) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process")
    public ResponseEntity<OrderEntity> process(@Valid @RequestBody ProcessOrderRequest request) {
        return ResponseEntity.ok(orderService.process(request));
    }

    @PostMapping("/cancel")
    public ResponseEntity<OrderEntity> cancel(@Valid @RequestBody CancelOrderRequest request) {
        return ResponseEntity.ok(orderService.cancel(request));
    }

    @PostMapping("/split-shipment")
    public ResponseEntity<List<Shipment>> splitShipment(@Valid @RequestBody SplitShipmentRequest request) {
        return ResponseEntity.ok(orderService.splitShipment(request));
    }

    @GetMapping("/{id}/shipments")
    public List<Shipment> getOrderShipments(@PathVariable Long id) {
        return orderService.getShipmentsByOrderId(id);
    }
}

