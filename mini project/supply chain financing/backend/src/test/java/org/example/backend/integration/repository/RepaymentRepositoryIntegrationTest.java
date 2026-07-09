package org.example.backend.integration.repository;

import org.example.backend.entity.*;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.RepaymentRepository;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RepaymentRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Test
    void findByLoanOrderByPaymentDateDescShouldSortDescending() {
        AppUser customer = userRepository.save(AppUser.builder()
                .name("Customer")
                .email("repayment-customer@test.local")
                .password("encoded")
                .role(Role.CUSTOMER)
                .phone("9999999999")
                .createdAt(LocalDateTime.now())
                .build());

        Loan loan = loanRepository.save(Loan.builder()
                .loanNumber("LN-REP-1")
                .customer(customer)
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("10.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .status(LoanStatus.DISBURSED)
                .remainingAmount(new BigDecimal("1100.00"))
                .createdAt(LocalDateTime.now())
                .build());

        repaymentRepository.save(Repayment.builder()
                .loan(loan)
                .amount(new BigDecimal("100.00"))
                .paymentDate(LocalDate.now().minusDays(5))
                .paymentMode(PaymentMode.NEFT)
                .remarks("older")
                .build());

        repaymentRepository.save(Repayment.builder()
                .loan(loan)
                .amount(new BigDecimal("200.00"))
                .paymentDate(LocalDate.now())
                .paymentMode(PaymentMode.UPI)
                .remarks("latest")
                .build());

        List<Repayment> repayments = repaymentRepository.findByLoanOrderByPaymentDateDesc(loan);

        assertThat(repayments).hasSize(2);
        assertThat(repayments.get(0).getRemarks()).isEqualTo("latest");
        assertThat(repayments.get(1).getRemarks()).isEqualTo("older");
    }
}

