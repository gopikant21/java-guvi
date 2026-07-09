package org.example.backend.dto.response;

import lombok.Builder;
import lombok.Value;
import org.example.backend.entity.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class LoanResponse {
    Long id;
    String loanNumber;
    Long customerId;
    String customerName;
    BigDecimal amount;
    BigDecimal interestRate;
    Integer tenureMonths;
    String purpose;
    LoanStatus status;
    Long approvedById;
    LocalDate approvedDate;
    LocalDate disbursedDate;
    BigDecimal remainingAmount;
    String rejectionReason;
    LocalDateTime createdAt;
}

