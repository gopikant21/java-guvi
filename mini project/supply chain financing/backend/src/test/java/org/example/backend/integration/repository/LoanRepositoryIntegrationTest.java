package org.example.backend.integration.repository;

import org.example.backend.entity.*;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class LoanRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void loanFiltersShouldReturnExpectedRows() {
        AppUser customer = userRepository.save(AppUser.builder()
                .name("Customer")
                .email("loan-customer@test.local")
                .password("encoded")
                .role(Role.CUSTOMER)
                .phone("9999999999")
                .createdAt(LocalDateTime.now())
                .build());

        Loan pending = saveLoan("LN-R1", customer, LoanStatus.PENDING);
        saveLoan("LN-R2", customer, LoanStatus.APPROVED);

        List<Loan> byCustomer = loanRepository.findByCustomerId(customer.getId());
        List<Loan> byStatus = loanRepository.findByStatus(LoanStatus.PENDING);
        List<Loan> byBoth = loanRepository.findByStatusAndCustomerId(LoanStatus.PENDING, customer.getId());

        assertThat(byCustomer).hasSize(2);
        assertThat(byStatus).extracting(Loan::getLoanNumber).contains("LN-R1");
        assertThat(byBoth).singleElement().extracting(Loan::getId).isEqualTo(pending.getId());
        assertThat(loanRepository.countByStatus(LoanStatus.PENDING)).isGreaterThanOrEqualTo(1L);
    }

    private Loan saveLoan(String loanNumber, AppUser customer, LoanStatus status) {
        return loanRepository.save(Loan.builder()
                .loanNumber(loanNumber)
                .customer(customer)
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("10.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .status(status)
                .remainingAmount(new BigDecimal("1100.00"))
                .createdAt(LocalDateTime.now())
                .build());
    }
}

