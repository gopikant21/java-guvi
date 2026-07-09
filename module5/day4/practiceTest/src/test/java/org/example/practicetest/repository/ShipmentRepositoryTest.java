package org.example.practicetest.repository;

import org.example.practicetest.entity.Merchant;
import org.example.practicetest.entity.OrderEntity;
import org.example.practicetest.entity.OrderStatus;
import org.example.practicetest.entity.Shipment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ShipmentRepositoryTest {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void shouldFindShipmentsByOrderId() {
        Merchant merchant = new Merchant();
        merchant.setName("XYZ");
        merchant.setEmail("ship@xyz.com");
        merchant.setTaxId("AB12CD34EF");
        merchant.setPassword("encoded");
        merchant = merchantRepository.saveAndFlush(merchant);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-SHIP-1");
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.TEN);
        order.setMerchant(merchant);
        order = orderRepository.saveAndFlush(order);

        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber("TRK-01");
        shipment.setCarrier("DHL");
        shipment.setShippingCost(BigDecimal.ONE);
        shipment.setShipmentDate(LocalDate.now());
        shipmentRepository.saveAndFlush(shipment);

        assertEquals(1, shipmentRepository.findByOrderId(order.getId()).size());
    }
}



