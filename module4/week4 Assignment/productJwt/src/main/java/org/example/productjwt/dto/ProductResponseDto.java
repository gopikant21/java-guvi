package org.example.productjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.productjwt.enums.ProductCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private Double price;
    private ProductCategory category;
    private String brand;
    private int stocks;
}

