package org.example.practicetest.service;

import org.example.practicetest.dto.order.CancelOrderRequest;
import org.example.practicetest.dto.order.ProcessOrderRequest;
import org.example.practicetest.dto.order.SplitShipmentRequest;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.Shipment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoopOrderService implements OrderService {
    @Override
    public OrderEntity create(OrderEntity order) {
        return order;
    }

    @Override
    public List<OrderEntity> getAll() {
        return List.of();
    }

    @Override
    public OrderEntity getById(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public OrderEntity update(Long id, OrderEntity order) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public OrderEntity process(ProcessOrderRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public OrderEntity cancel(CancelOrderRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Shipment> splitShipment(SplitShipmentRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Shipment> getShipmentsByOrderId(Long orderId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

