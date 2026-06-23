package org.example.productjpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTotalResponseDto {
    private Long orderId;
    private Double total;
}

