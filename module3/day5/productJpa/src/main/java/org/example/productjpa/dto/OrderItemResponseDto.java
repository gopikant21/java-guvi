package org.example.productjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private int quantity;
    private Double lineTotal;
}

