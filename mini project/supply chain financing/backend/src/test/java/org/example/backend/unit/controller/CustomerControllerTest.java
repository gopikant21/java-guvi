package org.example.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.controller.CustomerController;
import org.example.backend.dto.request.LoanApplicationRequest;
import org.example.backend.dto.request.RepaymentRequest;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.dto.response.UserProfileResponse;
import org.example.backend.entity.LoanStatus;
import org.example.backend.entity.PaymentMode;
import org.example.backend.entity.Role;
import org.example.backend.security.JwtAuthenticationFilter;
import org.example.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void profileShouldReturnCurrentCustomer() throws Exception {
        UserProfileResponse response = UserProfileResponse.builder()
                .id(1L)
                .name("Customer")
                .email("customer@test.local")
                .phone("9999999999")
                .role(Role.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .build();

        when(customerService.getProfile("customer@test.local")).thenReturn(response);

        mockMvc.perform(get("/api/customer/profile")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("customer@test.local"));
    }

    @Test
    void applyLoanShouldReturnCreated() throws Exception {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setAmount(new BigDecimal("100000"));
        request.setInterestRate(new BigDecimal("12"));
        request.setTenureMonths(12);
        request.setPurpose("Inventory");

        LoanResponse response = LoanResponse.builder()
                .id(10L)
                .loanNumber("LN-10")
                .status(LoanStatus.PENDING)
                .amount(new BigDecimal("100000.00"))
                .build();

        when(customerService.applyLoan("customer@test.local", request)).thenReturn(response);

        mockMvc.perform(post("/api/customer/loans")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanNumber").value("LN-10"));
    }

    @Test
    void repayLoanShouldReturnUpdatedLoan() throws Exception {
        RepaymentRequest request = new RepaymentRequest();
        request.setAmount(new BigDecimal("500.00"));
        request.setPaymentMode(PaymentMode.NEFT);
        request.setRemarks("installment");

        LoanResponse response = LoanResponse.builder()
                .id(10L)
                .status(LoanStatus.PARTIALLY_PAID)
                .remainingAmount(new BigDecimal("500.00"))
                .build();

        when(customerService.repayLoan("customer@test.local", 10L, request)).thenReturn(response);

        mockMvc.perform(post("/api/customer/loans/10/repay")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PARTIALLY_PAID"));
    }

    @Test
    void myLoansShouldReturnList() throws Exception {
        LoanResponse loan = LoanResponse.builder().id(1L).loanNumber("LN-1").status(LoanStatus.PENDING).build();
        when(customerService.getMyLoans("customer@test.local")).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/customer/loans")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(auth())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanNumber").value("LN-1"));
    }

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(
                "customer@test.local",
                "n/a",
                List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }
}



