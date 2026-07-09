package org.example.backend.unit.service;

import org.example.backend.dto.response.DashboardResponse;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.entity.AppUser;
import org.example.backend.entity.Loan;
import org.example.backend.entity.LoanStatus;
import org.example.backend.entity.Role;
import org.example.backend.exception.BadRequestException;
import org.example.backend.repository.LoanRepository;
import org.example.backend.repository.RepaymentRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private RepaymentRepository repaymentRepository;

    @InjectMocks
    private AdminService adminService;

    private AppUser admin;
    private AppUser customer;

    @BeforeEach
    void setUp() {
        admin = AppUser.builder()
                .id(100L)
                .name("Admin")
                .email("admin@test.local")
                .password("encoded")
                .role(Role.ADMIN)
                .phone("9000000000")
                .createdAt(LocalDateTime.now())
                .build();

        customer = AppUser.builder()
                .id(1L)
                .name("Customer")
                .email("customer@test.local")
                .password("encoded")
                .role(Role.CUSTOMER)
                .phone("9999999999")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void dashboardShouldReturnAggregatedCountsAndAmounts() {
        Loan disbursed = loan(1L, LoanStatus.DISBURSED, new BigDecimal("1000.00"));
        Loan pending = loan(2L, LoanStatus.PENDING, new BigDecimal("2000.00"));
        Loan fullyPaid = loan(3L, LoanStatus.FULLY_PAID, new BigDecimal("1500.00"));

        when(userRepository.findByRole(Role.CUSTOMER)).thenReturn(List.of(customer));
        when(loanRepository.findAll()).thenReturn(List.of(disbursed, pending, fullyPaid));
        when(loanRepository.countByStatus(LoanStatus.PENDING)).thenReturn(1L);
        when(loanRepository.countByStatus(LoanStatus.APPROVED)).thenReturn(0L);
        when(loanRepository.countByStatus(LoanStatus.REJECTED)).thenReturn(0L);
        when(loanRepository.countByStatus(LoanStatus.DISBURSED)).thenReturn(1L);
        when(loanRepository.countByStatus(LoanStatus.CLOSED)).thenReturn(0L);

        DashboardResponse dashboard = adminService.dashboard();

        assertThat(dashboard.getTotalCustomers()).isEqualTo(1L);
        assertThat(dashboard.getTotalLoans()).isEqualTo(3L);
        assertThat(dashboard.getPendingLoans()).isEqualTo(1L);
        assertThat(dashboard.getDisbursedLoans()).isEqualTo(1L);
        assertThat(dashboard.getTotalAmountDisbursed()).isEqualByComparingTo("2500.00");
    }

    @Test
    void approveLoanShouldSetApprovalData() {
        Loan pendingLoan = loan(10L, LoanStatus.PENDING, new BigDecimal("5000.00"));

        when(loanRepository.findById(10L)).thenReturn(Optional.of(pendingLoan));
        when(userRepository.findByEmail("admin@test.local")).thenReturn(Optional.of(admin));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse response = adminService.approveLoan(10L, "admin@test.local");

        assertThat(response.getStatus()).isEqualTo(LoanStatus.APPROVED);
        assertThat(response.getApprovedById()).isEqualTo(100L);
        assertThat(response.getApprovedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void rejectLoanShouldFailForNonPendingLoan() {
        Loan approvedLoan = loan(11L, LoanStatus.APPROVED, new BigDecimal("5000.00"));
        when(loanRepository.findById(11L)).thenReturn(Optional.of(approvedLoan));

        assertThatThrownBy(() -> adminService.rejectLoan(11L, "late"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Only pending loan can be rejected");
    }

    @Test
    void disburseLoanShouldMoveApprovedLoanToDisbursed() {
        Loan approvedLoan = loan(12L, LoanStatus.APPROVED, new BigDecimal("5000.00"));

        when(loanRepository.findById(12L)).thenReturn(Optional.of(approvedLoan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LoanResponse response = adminService.disburseLoan(12L);

        assertThat(response.getStatus()).isEqualTo(LoanStatus.DISBURSED);
        assertThat(response.getDisbursedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void getAllLoansShouldUseCombinedFilterWhenBothProvided() {
        when(loanRepository.findByStatusAndCustomerId(LoanStatus.PENDING, 1L))
                .thenReturn(List.of(loan(22L, LoanStatus.PENDING, new BigDecimal("1000.00"))));

        List<LoanResponse> responses = adminService.getAllLoans(LoanStatus.PENDING, 1L);

        verify(loanRepository).findByStatusAndCustomerId(LoanStatus.PENDING, 1L);
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getStatus()).isEqualTo(LoanStatus.PENDING);
    }

    private Loan loan(Long id, LoanStatus status, BigDecimal amount) {
        return Loan.builder()
                .id(id)
                .loanNumber("LN-" + id)
                .customer(customer)
                .amount(amount)
                .interestRate(new BigDecimal("10.00"))
                .tenureMonths(12)
                .purpose("Inventory")
                .status(status)
                .remainingAmount(amount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}

