package org.example.backend.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.request.RejectLoanRequest;
import org.example.backend.dto.response.*;
import org.example.backend.entity.LoanStatus;
import org.example.backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin", description = "Admin dashboard and loan lifecycle management")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard() {
        log.debug("Admin dashboard requested");
        return ResponseEntity.ok(adminService.dashboard());
    }

    @GetMapping("/customers")
    public ResponseEntity<List<UserProfileResponse>> allCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDetailsResponse> customerDetails(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCustomerDetails(id));
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanResponse>> allLoans(@RequestParam(required = false) LoanStatus status,
                                                        @RequestParam(required = false) Long customerId) {
        return ResponseEntity.ok(adminService.getAllLoans(status, customerId));
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<AdminLoanDetailsResponse> loanDetails(@PathVariable Long loanId) {
        return ResponseEntity.ok(adminService.getLoanDetails(loanId));
    }

    @PutMapping("/loans/{loanId}/approve")
    public ResponseEntity<LoanResponse> approveLoan(@PathVariable Long loanId, Authentication authentication) {
        log.info("Admin={} approving loanId={}", authentication.getName(), loanId);
        return ResponseEntity.ok(adminService.approveLoan(loanId, authentication.getName()));
    }

    @PutMapping("/loans/{loanId}/reject")
    public ResponseEntity<LoanResponse> rejectLoan(@PathVariable Long loanId,
                                                   @Valid @RequestBody RejectLoanRequest request) {
        log.info("Rejecting loanId={} reason={}", loanId, request.getReason());
        return ResponseEntity.ok(adminService.rejectLoan(loanId, request.getReason()));
    }

    @PutMapping("/loans/{loanId}/disburse")
    public ResponseEntity<LoanResponse> disburseLoan(@PathVariable Long loanId) {
        log.info("Disbursing loanId={}", loanId);
        return ResponseEntity.ok(adminService.disburseLoan(loanId));
    }

    @GetMapping("/loans/{loanId}/repayments")
    public ResponseEntity<List<RepaymentResponse>> loanRepayments(@PathVariable Long loanId) {
        return ResponseEntity.ok(adminService.getLoanRepayments(loanId));
    }
}

