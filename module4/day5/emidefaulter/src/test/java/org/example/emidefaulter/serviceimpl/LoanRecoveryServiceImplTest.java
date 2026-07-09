package org.example.emidefaulter.serviceimpl;

import jakarta.validation.ValidationException;
import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.dto.DashboardDTO;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.CustomerRole;
import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.LoanStatus;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.entity.PaymentStatus;
import org.example.emidefaulter.exception.CustomerNotFoundException;
import org.example.emidefaulter.exception.DuplicateResourceException;
import org.example.emidefaulter.exception.InvalidLoanStatusException;
import org.example.emidefaulter.exception.InvalidOperationException;
import org.example.emidefaulter.exception.PenaltyNotFoundException;
import org.example.emidefaulter.repository.CustomerRepository;
import org.example.emidefaulter.repository.EmiPaymentRepository;
import org.example.emidefaulter.repository.LoanRepository;
import org.example.emidefaulter.repository.PenaltyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanRecoveryServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EmiPaymentRepository emiPaymentRepository;

    @Mock
    private PenaltyRepository penaltyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoanRecoveryServiceImpl service;

    @Test
    void registerCustomerEncodesPasswordAndSavesCustomer() {
        Customer customer = Customer.builder()
                .customerName("John")
                .email("john@example.com")
                .password("plain-pass")
                .phoneNumber("9876543210")
                .city("Mumbai")
                .creditScore(750)
                .role(CustomerRole.CUSTOMER)
                .build();

        when(passwordEncoder.encode("plain-pass")).thenReturn("encoded-pass");
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer saved = service.registerCustomer(customer);

        assertEquals("encoded-pass", saved.getPassword());
        verify(passwordEncoder, times(1)).encode("plain-pass");
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void registerCustomerThrowsWhenEmailAlreadyExists() {
        Customer customer = Customer.builder()
                .email("john@example.com")
                .password("plain-pass")
                .build();

        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(Customer.builder().build()));

        assertThrows(DuplicateResourceException.class, () -> service.registerCustomer(customer));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomerThrowsWhenCustomerDoesNotExist() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> service.deleteCustomer(99L));
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void updateLoanStatusReturnsUpdatedLoan() {
        Loan loan = Loan.builder().loanId(10L).loanStatus(LoanStatus.ACTIVE).build();
        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        Loan updated = service.updateLoanStatus(10L, LoanStatus.DEFAULTED);

        assertEquals(LoanStatus.DEFAULTED, updated.getLoanStatus());
    }

    @Test
    void updateLoanStatusThrowsWhenStatusIsInvalid() {
        Loan loan = Loan.builder().loanId(10L).loanStatus(LoanStatus.ACTIVE).build();
        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));

        assertThrows(InvalidLoanStatusException.class, () -> service.updateLoanStatus(10L, null));
    }

    @Test
    void generatePenaltyCreatesPenaltyWithInputValues() {
        Loan loan = Loan.builder().loanId(5L).emiAmount(2000.0).build();
        EmiPayment payment = EmiPayment.builder()
                .paymentId(7L)
                .loan(loan)
                .amountPaid(0.0)
                .paymentStatus(PaymentStatus.PENDING)
                .dueDate(LocalDate.now().minusDays(3))
                .build();

        when(emiPaymentRepository.findById(7L)).thenReturn(Optional.of(payment));
        when(penaltyRepository.save(any(Penalty.class))).thenAnswer(invocation -> {
            Penalty p = invocation.getArgument(0);
            p.setPenaltyId(101L);
            return p;
        });

        Penalty result = service.generatePenalty(7L, 450.0, "Manual penalty");

        assertNotNull(result.getGeneratedDate());
        assertEquals(101L, result.getPenaltyId());
        assertEquals(450.0, result.getPenaltyAmount());
        assertEquals("Manual penalty", result.getPenaltyReason());
    }

    @Test
    void generatePenaltyThrowsWhenPaymentStatusIsPaid() {
        Loan loan = Loan.builder().loanId(5L).emiAmount(2000.0).build();
        EmiPayment payment = EmiPayment.builder()
                .paymentId(7L)
                .loan(loan)
                .amountPaid(2000.0)
                .paymentStatus(PaymentStatus.PAID)
                .dueDate(LocalDate.now().minusDays(3))
                .build();

        when(emiPaymentRepository.findById(7L)).thenReturn(Optional.of(payment));

        assertThrows(InvalidOperationException.class, () -> service.generatePenalty(7L, 450.0, "Manual penalty"));
    }

    @Test
    void getLatestPenaltyGeneratedThrowsWhenNoPenaltyRecordExists() {
        when(penaltyRepository.findLatestPenaltyRecords(PageRequest.of(0, 1))).thenReturn(List.of());

        assertThrows(PenaltyNotFoundException.class, () -> service.getLatestPenaltyGenerated());
    }

    @Test
    void increaseInterestRateThrowsValidationExceptionForNonPositivePercentage() {
        assertThrows(ValidationException.class, () -> service.increaseInterestRate(0));
        verify(loanRepository, never()).increaseInterestRate(anyDouble());
    }

    @Test
    void getDashboardBuildsExpectedMetrics() {
        when(customerRepository.count()).thenReturn(3L);
        when(loanRepository.countByLoanStatus(LoanStatus.ACTIVE)).thenReturn(2L);
        when(loanRepository.countByLoanStatus(LoanStatus.DEFAULTED)).thenReturn(1L);
        when(loanRepository.getOutstandingAmountCityWise()).thenReturn(List.of(
                new CityOutstandingDTO("Mumbai", 1500.0),
                new CityOutstandingDTO("Pune", 500.0)
        ));
        when(penaltyRepository.sumTotalPenaltyCollected()).thenReturn(300.0);
        when(customerRepository.findTopDefaulters(PageRequest.of(0, 1))).thenReturn(List.of(
                new CustomerPendingEmiDTO(1L, "Alice", 1200.0)
        ));
        when(loanRepository.findCityWithHighestDefaults(PageRequest.of(0, 1))).thenReturn(List.of("Mumbai"));
        when(customerRepository.findAverageCreditScore()).thenReturn(741.0);
        when(emiPaymentRepository.sumTotalAmountPaid()).thenReturn(2000.0);
        when(emiPaymentRepository.sumTotalDueAmount()).thenReturn(2500.0);

        DashboardDTO dashboard = service.getDashboard();

        assertEquals(3L, dashboard.totalCustomers());
        assertEquals(2L, dashboard.activeLoans());
        assertEquals(1L, dashboard.defaultedLoans());
        assertEquals(2000.0, dashboard.totalOutstandingAmount());
        assertEquals("Alice", dashboard.highestDefaulter());
        assertEquals(80.0, dashboard.recoveryRate());
    }

    @Test
    void runMissedEmiSchedulerMarksPaymentAndDefaultsLoanAfterThreeConsecutiveMissed() {
        Loan loan = Loan.builder()
                .loanId(20L)
                .loanStatus(LoanStatus.ACTIVE)
                .emiAmount(2000.0)
                .build();

        EmiPayment latestOverdue = EmiPayment.builder()
                .paymentId(1L)
                .loan(loan)
                .dueDate(LocalDate.now().minusDays(1))
                .amountPaid(0.0)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        EmiPayment second = EmiPayment.builder()
                .paymentId(2L)
                .loan(loan)
                .dueDate(LocalDate.now().minusDays(2))
                .amountPaid(0.0)
                .paymentStatus(PaymentStatus.MISSED)
                .build();
        EmiPayment third = EmiPayment.builder()
                .paymentId(3L)
                .loan(loan)
                .dueDate(LocalDate.now().minusDays(3))
                .amountPaid(0.0)
                .paymentStatus(PaymentStatus.MISSED)
                .build();

        loan.setEmiPayments(List.of(latestOverdue, second, third));

        when(emiPaymentRepository.findOverduePendingPayments(any(LocalDate.class))).thenReturn(List.of(latestOverdue));
        when(penaltyRepository.findFirstByPaymentPaymentId(1L)).thenReturn(Optional.empty());

        service.runMissedEmiScheduler();

        assertEquals(PaymentStatus.MISSED, latestOverdue.getPaymentStatus());
        assertEquals(LoanStatus.DEFAULTED, loan.getLoanStatus());

        ArgumentCaptor<Penalty> penaltyCaptor = ArgumentCaptor.forClass(Penalty.class);
        verify(penaltyRepository, times(1)).save(penaltyCaptor.capture());
        assertEquals(100.0, penaltyCaptor.getValue().getPenaltyAmount());
        assertEquals(1L, penaltyCaptor.getValue().getPayment().getPaymentId());
    }
}


