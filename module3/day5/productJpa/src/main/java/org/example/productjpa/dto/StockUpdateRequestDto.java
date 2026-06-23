package org.example.productjpa.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequestDto {

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}

