package org.example.practicetest.integration;

import org.example.practicetest.entity.Merchant;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.OrderStatus;
import org.example.practicetest.entity.Shipment;
import org.example.practicetest.repository.MerchantRepository;
import org.example.practicetest.repository.OrderRepository;
import org.example.practicetest.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class FulfillmentIntegrationTest {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void processOrderShouldUpdateStatusAndCreateShipment() {
        OrderEntity order = persistedPendingOrder("ORD-PROC-1");

        order.setOrderStatus(OrderStatus.PROCESSED);
        orderRepository.saveAndFlush(order);

        Shipment shipment = shipment(order, "TRK-1");
        shipmentRepository.saveAndFlush(shipment);

        OrderEntity updated = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals(OrderStatus.PROCESSED, updated.getOrderStatus());
        assertEquals(1, shipmentRepository.findByOrderId(order.getId()).size());
    }

    @Test
    void splitShipmentShouldCreateTwoDistinctShipmentRecords() {
        OrderEntity order = persistedPendingOrder("ORD-SPLIT-1");

        shipmentRepository.saveAndFlush(shipment(order, "TRK-A"));
        shipmentRepository.saveAndFlush(shipment(order, "TRK-B"));

        List<Shipment> shipments = shipmentRepository.findByOrderId(order.getId());
        assertEquals(2, shipments.size());
    }

    @Test
    void splitShipmentShouldBeAtomicWhenErrorOccurs() {
        TransactionTemplate tx = new TransactionTemplate(transactionManager);
        tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        Long orderId = tx.execute(status -> persistedPendingOrder("ORD-ATOMIC-1").getId());

        assertThrows(IllegalArgumentException.class, () -> tx.executeWithoutResult(status -> {
            OrderEntity order = orderRepository.findById(orderId).orElseThrow();
            shipmentRepository.saveAndFlush(shipment(order, "TRK-ROLLBACK-1"));
            throw new IllegalArgumentException("invalid split package");
        }));

        assertEquals(0, shipmentRepository.findByOrderId(orderId).size());
    }

    private OrderEntity persistedPendingOrder(String number) {
        Merchant merchant = new Merchant();
        merchant.setName("XYZ");
        merchant.setEmail(number.toLowerCase() + "@xyz.com");
        merchant.setTaxId("AB12CD34EF");
        merchant.setPassword("encoded");
        merchant = merchantRepository.saveAndFlush(merchant);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber(number);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setMerchant(merchant);
        return orderRepository.saveAndFlush(order);
    }

    private Shipment shipment(OrderEntity order, String tracking) {
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(tracking);
        shipment.setCarrier("DHL");
        shipment.setShippingCost(BigDecimal.valueOf(5));
        shipment.setShipmentDate(LocalDate.now());
        return shipment;
    }
}


