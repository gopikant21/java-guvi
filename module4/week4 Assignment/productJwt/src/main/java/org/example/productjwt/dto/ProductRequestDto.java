package org.example.productjwt.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.productjwt.enums.ProductCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    private String brand;

    @Min(value = 0, message = "Stocks cannot be negative")
    private Integer stocks;
}

