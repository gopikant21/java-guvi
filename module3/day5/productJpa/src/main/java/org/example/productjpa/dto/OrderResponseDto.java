package org.example.productjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private Double total;
}

