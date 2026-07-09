package org.example.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanApplicationRequest {
    @NotNull
    @DecimalMin(value = "1.0")
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate;

    @NotNull
    @Min(1)
    private Integer tenureMonths;

    @NotBlank
    private String purpose;
}

