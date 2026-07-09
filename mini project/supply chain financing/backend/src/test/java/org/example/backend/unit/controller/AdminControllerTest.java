package org.example.backend.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.controller.AdminController;
import org.example.backend.dto.request.RejectLoanRequest;
import org.example.backend.dto.response.DashboardResponse;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.entity.LoanStatus;
import org.example.backend.security.JwtAuthenticationFilter;
import org.example.backend.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void dashboardShouldReturnMetrics() throws Exception {
        DashboardResponse response = DashboardResponse.builder()
                .totalCustomers(5)
                .totalLoans(8)
                .pendingLoans(2)
                .approvedLoans(2)
                .rejectedLoans(1)
                .disbursedLoans(2)
                .closedLoans(1)
                .totalAmountDisbursed(new BigDecimal("1200000.00"))
                .build();

        when(adminService.dashboard()).thenReturn(response);

        mockMvc.perform(get("/api/admin/dashboard")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.local").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(5));
    }

    @Test
    void allLoansShouldReturnFilteredList() throws Exception {
        LoanResponse response = LoanResponse.builder().id(2L).status(LoanStatus.PENDING).loanNumber("LN-2").build();
        when(adminService.getAllLoans(LoanStatus.PENDING, null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/loans")
                        .param("status", "PENDING")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.local").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void rejectLoanShouldReturnUpdatedLoan() throws Exception {
        RejectLoanRequest request = new RejectLoanRequest();
        request.setReason("Low score");

        LoanResponse response = LoanResponse.builder()
                .id(10L)
                .status(LoanStatus.REJECTED)
                .rejectionReason("Low score")
                .build();

        when(adminService.rejectLoan(10L, "Low score")).thenReturn(response);

        mockMvc.perform(put("/api/admin/loans/10/reject")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin@test.local").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}


