package org.example.productjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.productjwt.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private Double total;
}

