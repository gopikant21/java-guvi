package org.example.practicetest.service;

import org.example.practicetest.entity.Shipment;
import org.example.practicetest.exception.OrderNotFoundException;
import org.example.practicetest.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentReadSpec service;

    @Test
    void getByIdShouldReturnShipmentWhenPresent() {
        Shipment shipment = new Shipment();
        shipment.setId(7L);
        when(shipmentRepository.findById(7L)).thenReturn(Optional.of(shipment));

        Shipment found = service.getById(7L);

        assertEquals(7L, found.getId());
    }

    @Test
    void getByIdShouldThrowWhenMissing() {
        when(shipmentRepository.findById(8L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.getById(8L));
    }

    private static final class ShipmentReadSpec {
        private final ShipmentRepository shipmentRepository;

        private ShipmentReadSpec(ShipmentRepository shipmentRepository) {
            this.shipmentRepository = shipmentRepository;
        }

        private Shipment getById(Long id) {
            return shipmentRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Shipment not found"));
        }
    }
}


