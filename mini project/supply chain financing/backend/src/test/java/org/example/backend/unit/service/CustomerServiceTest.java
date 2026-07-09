package org.example.backend.unit.service;

import org.example.backend.dto.request.LoanApplicationRequest;
import org.example.backend.dto.request.RepaymentRequest;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.entity.*;
import org.example.backend.exception.BadRequestException;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.RepaymentRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private RepaymentRepository repaymentRepository;

    @InjectMocks
    private CustomerService customerService;

    private AppUser customer;

    @BeforeEach
    void setUp() {
        customer = AppUser.builder()
                .id(1L)
                .name("Customer")
                .email("customer@example.com")
                .password("encoded")
                .role(Role.CUSTOMER)
                .phone("9999999999")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void applyLoanShouldCreatePendingLoanWithCalculatedRemainingAmount() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setAmount(new BigDecimal("100000"));
        request.setInterestRate(new BigDecimal("12"));
        request.setTenureMonths(12);
        request.setPurpose("Working Capital");

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(10L);
            return loan;
        });

        LoanResponse response = customerService.applyLoan("customer@example.com", request);

        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(captor.capture());

        Loan savedLoan = captor.getValue();
        assertThat(savedLoan.getStatus()).isEqualTo(LoanStatus.PENDING);
        assertThat(savedLoan.getRemainingAmount()).isEqualByComparingTo("112000.00");
        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void repayLoanShouldUpdateStatusToPartiallyPaid() {
        Loan loan = Loan.builder()
                .id(11L)
                .loanNumber("LN-TEST123")
                .customer(customer)
                .status(LoanStatus.DISBURSED)
                .remainingAmount(new BigDecimal("1000.00"))
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("10.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .createdAt(LocalDateTime.now())
                .build();

        RepaymentRequest request = new RepaymentRequest();
        request.setAmount(new BigDecimal("250.00"));
        request.setPaymentMode(PaymentMode.UPI);
        request.setRemarks("installment");

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(loanRepository.findById(11L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse response = customerService.repayLoan("customer@example.com", 11L, request);

        verify(repaymentRepository).save(any(Repayment.class));
        assertThat(response.getStatus()).isEqualTo(LoanStatus.PARTIALLY_PAID);
        assertThat(response.getRemainingAmount()).isEqualByComparingTo("750.00");
    }

    @Test
    void repayLoanShouldMarkFullyPaidWhenRemainingBecomesZero() {
        Loan loan = Loan.builder()
                .id(12L)
                .loanNumber("LN-TEST124")
                .customer(customer)
                .status(LoanStatus.PARTIALLY_PAID)
                .remainingAmount(new BigDecimal("300.00"))
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("10.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .createdAt(LocalDateTime.now())
                .build();

        RepaymentRequest request = new RepaymentRequest();
        request.setAmount(new BigDecimal("300.00"));
        request.setPaymentMode(PaymentMode.NEFT);

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(loanRepository.findById(12L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse response = customerService.repayLoan("customer@example.com", 12L, request);

        assertThat(response.getStatus()).isEqualTo(LoanStatus.FULLY_PAID);
        assertThat(response.getRemainingAmount()).isEqualByComparingTo("0.00");
    }

    @Test
    void repayLoanShouldFailWhenLoanNotRepayable() {
        Loan loan = Loan.builder()
                .id(13L)
                .customer(customer)
                .status(LoanStatus.PENDING)
                .remainingAmount(new BigDecimal("1000.00"))
                .amount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("12.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .loanNumber("LN-LOCKED")
                .createdAt(LocalDateTime.now())
                .build();

        RepaymentRequest request = new RepaymentRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setPaymentMode(PaymentMode.CASH);

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customer));
        when(loanRepository.findById(13L)).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> customerService.repayLoan("customer@example.com", 13L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Loan is not eligible for repayment");

        verify(repaymentRepository, never()).save(any());
    }
}

