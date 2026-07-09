package org.example.backend.controller;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.request.LoanApplicationRequest;
import org.example.backend.dto.request.RepaymentRequest;
import org.example.backend.dto.request.UpdateProfileRequest;
import org.example.backend.dto.response.LoanResponse;
import org.example.backend.dto.response.RepaymentResponse;
import org.example.backend.dto.response.UserProfileResponse;
import org.example.backend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer", description = "Customer profile, loans and repayments")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> profile(Authentication authentication) {
        log.debug("Fetching profile for customer={}", authentication.getName());
        return ResponseEntity.ok(customerService.getProfile(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(Authentication authentication,
                                                             @Valid @RequestBody UpdateProfileRequest request) {
        log.info("Updating profile for customer={}", authentication.getName());
        return ResponseEntity.ok(customerService.updateProfile(authentication.getName(), request));
    }

    @PostMapping("/loans")
    public ResponseEntity<LoanResponse> applyLoan(Authentication authentication,
                                                  @Valid @RequestBody LoanApplicationRequest request) {
        log.info("Loan application received for customer={} amount={}", authentication.getName(), request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.applyLoan(authentication.getName(), request));
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanResponse>> myLoans(Authentication authentication) {
        return ResponseEntity.ok(customerService.getMyLoans(authentication.getName()));
    }

    @GetMapping("/loans/{loanId}")
    public ResponseEntity<LoanResponse> myLoanDetails(Authentication authentication,
                                                      @PathVariable Long loanId) {
        return ResponseEntity.ok(customerService.getMyLoanDetails(authentication.getName(), loanId));
    }

    @PostMapping("/loans/{loanId}/repay")
    public ResponseEntity<LoanResponse> repay(Authentication authentication,
                                              @PathVariable Long loanId,
                                              @Valid @RequestBody RepaymentRequest request) {
        log.info("Repayment request customer={} loanId={} amount={}", authentication.getName(), loanId, request.getAmount());
        return ResponseEntity.ok(customerService.repayLoan(authentication.getName(), loanId, request));
    }

    @GetMapping("/loans/{loanId}/repayments")
    public ResponseEntity<List<RepaymentResponse>> repaymentHistory(Authentication authentication,
                                                                    @PathVariable Long loanId) {
        return ResponseEntity.ok(customerService.getRepayments(authentication.getName(), loanId));
    }
}

