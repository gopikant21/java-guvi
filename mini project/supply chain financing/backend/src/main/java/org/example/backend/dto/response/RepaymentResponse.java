package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;
import org.example.backend.entity.PaymentMode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class RepaymentResponse {
    Long id;
    Long loanId;
    BigDecimal amount;
    LocalDate paymentDate;
    PaymentMode paymentMode;
    String remarks;
}

