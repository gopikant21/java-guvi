package org.example.practicetest.service;

import org.example.practicetest.dto.order.CancelOrderRequest;
import org.example.practicetest.dto.order.ProcessOrderRequest;
import org.example.practicetest.dto.order.SplitShipmentRequest;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.Shipment;

import java.util.List;

public interface OrderService {
    OrderEntity create(OrderEntity order);

    List<OrderEntity> getAll();

    OrderEntity getById(Long id);

    OrderEntity update(Long id, OrderEntity order);

    void delete(Long id);

    OrderEntity process(ProcessOrderRequest request);

    OrderEntity cancel(CancelOrderRequest request);

    List<Shipment> splitShipment(SplitShipmentRequest request);

    List<Shipment> getShipmentsByOrderId(Long orderId);
}

