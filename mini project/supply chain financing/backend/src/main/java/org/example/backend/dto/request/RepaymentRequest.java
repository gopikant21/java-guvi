package org.example.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.backend.entity.PaymentMode;

import java.math.BigDecimal;

@Data
public class RepaymentRequest {
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private PaymentMode paymentMode;

    private String remarks;
}

