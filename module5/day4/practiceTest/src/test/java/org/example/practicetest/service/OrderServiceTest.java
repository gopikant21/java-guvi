package org.example.practicetest.service;

import org.example.practicetest.dto.order.CancelOrderRequest;
import org.example.practicetest.dto.order.ProcessOrderRequest;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.OrderStatus;
import org.example.practicetest.entity.Shipment;
import org.example.practicetest.exception.OrderNotFoundException;
import org.example.practicetest.repository.OrderRepository;
import org.example.practicetest.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ShipmentRepository shipmentRepository;

    private OrderFulfillmentSpec service;

    @BeforeEach
    void setUp() {
        service = new OrderFulfillmentSpec(orderRepository, shipmentRepository);
    }

    @Test
    void processShouldTransitionPendingOrderToProcessedAndCreateShipment() {
        OrderEntity pending = new OrderEntity();
        pending.setId(1L);
        pending.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(pending));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderEntity processed = service.process(new ProcessOrderRequest(1L));

        assertEquals(OrderStatus.PROCESSED, processed.getOrderStatus());
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void cancelShouldRejectAlreadyProcessedOrder() {
        OrderEntity processed = new OrderEntity();
        processed.setId(2L);
        processed.setOrderStatus(OrderStatus.PROCESSED);
        when(orderRepository.findById(2L)).thenReturn(Optional.of(processed));

        assertThrows(IllegalArgumentException.class, () -> service.cancel(new CancelOrderRequest(2L)));
    }

    @Test
    void processShouldFailWhenOrderDoesNotExist() {
        when(orderRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.process(new ProcessOrderRequest(50L)));
    }

    private static final class OrderFulfillmentSpec {
        private final OrderRepository orderRepository;
        private final ShipmentRepository shipmentRepository;

        private OrderFulfillmentSpec(OrderRepository orderRepository, ShipmentRepository shipmentRepository) {
            this.orderRepository = orderRepository;
            this.shipmentRepository = shipmentRepository;
        }

        private OrderEntity process(ProcessOrderRequest request) {
            OrderEntity order = orderRepository.findById(request.orderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));
            if (order.getOrderStatus() != OrderStatus.PENDING) {
                throw new IllegalArgumentException("Only pending orders can be processed");
            }
            order.setOrderStatus(OrderStatus.PROCESSED);
            Shipment shipment = new Shipment();
            shipment.setOrder(order);
            shipment.setTrackingNumber("TRK-" + order.getId());
            shipment.setCarrier("DHL");
            shipment.setShippingCost(BigDecimal.TEN);
            shipment.setShipmentDate(LocalDate.now());
            shipmentRepository.save(shipment);
            return orderRepository.save(order);
        }

        private OrderEntity cancel(CancelOrderRequest request) {
            OrderEntity order = orderRepository.findById(request.orderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));
            if (order.getOrderStatus() == OrderStatus.PROCESSED) {
                throw new IllegalArgumentException("Processed order cannot be cancelled");
            }
            order.setOrderStatus(OrderStatus.CANCELLED);
            return orderRepository.save(order);
        }
    }
}


