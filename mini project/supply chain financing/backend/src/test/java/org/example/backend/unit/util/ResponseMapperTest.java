package org.example.backend.unit.util;

import org.example.backend.dto.response.LoanResponse;
import org.example.backend.dto.response.RepaymentResponse;
import org.example.backend.dto.response.UserProfileResponse;
import org.example.backend.entity.*;
import org.example.backend.util.ResponseMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseMapperTest {

    @Test
    void toUserProfileShouldMapFields() {
        AppUser user = AppUser.builder()
                .id(1L)
                .name("Alice")
                .email("alice@test.local")
                .phone("9876543210")
                .role(Role.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .build();

        UserProfileResponse response = ResponseMapper.toUserProfile(user);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo("alice@test.local");
        assertThat(response.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    void toLoanResponseShouldMapApprovedByAsNullable() {
        AppUser customer = AppUser.builder().id(1L).name("Alice").build();
        Loan loan = Loan.builder()
                .id(100L)
                .loanNumber("LN-100")
                .customer(customer)
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("12.50"))
                .tenureMonths(12)
                .purpose("Inventory")
                .status(LoanStatus.PENDING)
                .remainingAmount(new BigDecimal("1125.00"))
                .createdAt(LocalDateTime.now())
                .build();

        LoanResponse response = ResponseMapper.toLoanResponse(loan);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getApprovedById()).isNull();
        assertThat(response.getStatus()).isEqualTo(LoanStatus.PENDING);
    }

    @Test
    void toRepaymentResponseShouldMapFields() {
        Loan loan = Loan.builder().id(10L).build();
        Repayment repayment = Repayment.builder()
                .id(2L)
                .loan(loan)
                .amount(new BigDecimal("500.00"))
                .paymentDate(LocalDate.now())
                .paymentMode(PaymentMode.UPI)
                .remarks("test")
                .build();

        RepaymentResponse response = ResponseMapper.toRepaymentResponse(repayment);

        assertThat(response.getLoanId()).isEqualTo(10L);
        assertThat(response.getPaymentMode()).isEqualTo(PaymentMode.UPI);
    }
}

