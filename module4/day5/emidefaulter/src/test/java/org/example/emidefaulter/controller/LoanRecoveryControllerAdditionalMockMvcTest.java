package org.example.emidefaulter.controller;

import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.dto.DashboardDTO;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.LoanStatus;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.exception.GlobalExceptionHandler;
import org.example.emidefaulter.security.JwtUtil;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanRecoveryControllerAdditionalMockMvcTest {

    private MockMvc mockMvc;
    private LoanRecoveryService loanRecoveryService;

    @BeforeEach
    void setUp() {
        loanRecoveryService = mock(LoanRecoveryService.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);

        LoanRecoveryController controller = new LoanRecoveryController(
                loanRecoveryService,
                authenticationManager,
                jwtUtil
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void deleteCustomerReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/customers/{customerId}", 1L))
                .andExpect(status().isNoContent());

        verify(loanRecoveryService, times(1)).deleteCustomer(1L);
    }

    @Test
    void updateLoanStatusReturnsUpdatedLoan() throws Exception {
        Loan updated = Loan.builder().loanId(7L).loanStatus(LoanStatus.DEFAULTED).build();
        when(loanRecoveryService.updateLoanStatus(7L, LoanStatus.DEFAULTED)).thenReturn(updated);

        mockMvc.perform(patch("/loans/{loanId}/status", 7L)
                        .param("status", "DEFAULTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(7))
                .andExpect(jsonPath("$.loanStatus").value("DEFAULTED"));

        verify(loanRecoveryService, times(1)).updateLoanStatus(7L, LoanStatus.DEFAULTED);
    }

    @Test
    void generatePenaltyReturnsCreatedResponse() throws Exception {
        Penalty penalty = Penalty.builder()
                .penaltyId(11L)
                .penaltyAmount(500.0)
                .penaltyReason("Manual penalty")
                .generatedDate(LocalDate.now())
                .build();

        when(loanRecoveryService.generatePenalty(5L, 500.0, "Manual penalty")).thenReturn(penalty);

        mockMvc.perform(post("/penalties/{paymentId}", 5L)
                        .param("amount", "500")
                        .param("reason", "Manual penalty"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.penaltyId").value(11))
                .andExpect(jsonPath("$.penaltyAmount").value(500.0));

        verify(loanRecoveryService, times(1)).generatePenalty(5L, 500.0, "Manual penalty");
    }

    @Test
    void increaseInterestRateReturnsUpdatedLoanCount() throws Exception {
        when(loanRecoveryService.increaseInterestRate(2.5)).thenReturn(4);

        mockMvc.perform(put("/loans/increase-interest")
                        .param("percentage", "2.5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated loans: 4"));

        verify(loanRecoveryService, times(1)).increaseInterestRate(2.5);
    }

    @Test
    void getTopDefaultersReturnsExpectedPayload() throws Exception {
        when(loanRecoveryService.getTopDefaulters(2)).thenReturn(List.of(
                new CustomerPendingEmiDTO(1L, "Alice", 1500.0),
                new CustomerPendingEmiDTO(2L, "Bob", 900.0)
        ));

        mockMvc.perform(get("/reports/top-defaulters")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerName").value("Alice"));

        verify(loanRecoveryService, times(1)).getTopDefaulters(2);
    }

    @Test
    void getDashboardReturnsMetricsObject() throws Exception {
        DashboardDTO dashboard = new DashboardDTO(
                10L,
                6L,
                2L,
                12000.0,
                800.0,
                "Alice",
                2300.0,
                "Mumbai",
                745,
                82.5
        );

        when(loanRecoveryService.getDashboard()).thenReturn(dashboard);

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(10))
                .andExpect(jsonPath("$.cityWithHighestDefaults").value("Mumbai"));

        verify(loanRecoveryService, times(1)).getDashboard();
    }

    @Test
    void getAllLoansReturnsBadRequestForInvalidSortField() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "badField")
                        .param("direction", "DESC"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}


