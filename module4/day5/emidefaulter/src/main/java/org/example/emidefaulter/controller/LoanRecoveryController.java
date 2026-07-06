package org.example.emidefaulter.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.emidefaulter.dto.CityOutstandingDTO;
import org.example.emidefaulter.dto.CustomerLoanSummaryDTO;
import org.example.emidefaulter.dto.CustomerPendingEmiDTO;
import org.example.emidefaulter.dto.DashboardDTO;
import org.example.emidefaulter.dto.LoginRequestDTO;
import org.example.emidefaulter.dto.LoginResponseDTO;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.EmiPayment;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.entity.Penalty;
import org.example.emidefaulter.security.JwtUtil;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "EMI Loan Management", description = "APIs for loan tracking, EMI payments, penalties, and defaulter management")
public class LoanRecoveryController {

    private final LoanRecoveryService loanRecoveryService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Customer Login", description = "Authenticate with email and password to receive JWT token")
    @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Login attempt for email: {}", request.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            Object principal = authentication.getPrincipal();
            if (!(principal instanceof UserDetails userDetails)) {
                log.error("Invalid authentication principal for email: {}", request.email());
                throw new AuthenticationException("Invalid authentication principal") {
                };
            }
            String token = jwtUtil.generateToken(userDetails);
            log.info("Login successful for email: {}", request.email());
            return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer", jwtUtil.getExpirySeconds()));
        } catch (Exception e) {
            log.error("Login failed for email: {}", request.email(), e);
            throw e;
        }
    }

    @PostMapping("/customers")
    @Operation(summary = "Register Customer", description = "Create a new customer account")
    @ApiResponse(responseCode = "201", description = "Customer registered successfully")
    public ResponseEntity<Customer> registerCustomer(@Valid @RequestBody Customer customer) {
        log.info("Registering new customer with email: {}", customer.getEmail());
        Customer registeredCustomer = loanRecoveryService.registerCustomer(customer);
        log.info("Customer registered successfully with ID: {}", registeredCustomer.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCustomer);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Customer", description = "Delete customer by ID (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Customer deleted successfully")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        try {
            loanRecoveryService.deleteCustomer(customerId);
            log.info("Customer deleted successfully with ID: {}", customerId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting customer with ID: {}", customerId, e);
            throw e;
        }
    }

    @DeleteMapping("/loans/{loanId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Loan", description = "Delete loan by ID (ADMIN only)")
    @ApiResponse(responseCode = "204", description = "Loan deleted successfully")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long loanId) {
        log.info("Deleting loan with ID: {}", loanId);
        try {
            loanRecoveryService.deleteLoan(loanId);
            log.info("Loan deleted successfully with ID: {}", loanId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting loan with ID: {}", loanId, e);
            throw e;
        }
    }

    @PatchMapping("/loans/{loanId}/status")
    @PreAuthorize("hasRole('RECOVERY_MANAGER')")
    @Operation(summary = "Update Loan Status", description = "Change loan status (RECOVERY_MANAGER only)")
    @ApiResponse(responseCode = "200", description = "Loan status updated")
    public ResponseEntity<Loan> updateLoanStatus(@PathVariable Long loanId, @RequestParam @NotBlank String status) {
        log.info("Updating loan status for loan ID: {} to status: {}", loanId, status);
        try {
            Loan updatedLoan = loanRecoveryService.updateLoanStatus(loanId, status);
            log.info("Loan status updated successfully for loan ID: {}", loanId);
            return ResponseEntity.ok(updatedLoan);
        } catch (Exception e) {
            log.error("Error updating loan status for loan ID: {}", loanId, e);
            throw e;
        }
    }

    @PostMapping("/penalties/{paymentId}")
    @PreAuthorize("hasRole('RECOVERY_MANAGER')")
    @Operation(summary = "Generate Penalty", description = "Create penalty for missed EMI (RECOVERY_MANAGER only)")
    @ApiResponse(responseCode = "201", description = "Penalty generated successfully")
    public ResponseEntity<Penalty> generatePenalty(@PathVariable Long paymentId,
                                                   @RequestParam(defaultValue = "500") @Positive Double amount,
                                                   @RequestParam(defaultValue = "Manual penalty") String reason) {
        log.info("Generating penalty for payment ID: {} with amount: {} and reason: {}", paymentId, amount, reason);
        try {
            Penalty penalty = loanRecoveryService.generatePenalty(paymentId, amount, reason);
            log.info("Penalty generated successfully with ID: {}", penalty.getPenaltyId());
            return ResponseEntity.status(HttpStatus.CREATED).body(penalty);
        } catch (Exception e) {
            log.error("Error generating penalty for payment ID: {}", paymentId, e);
            throw e;
        }
    }

    @PutMapping("/loans/increase-interest")
    @PreAuthorize("hasRole('RECOVERY_MANAGER')")
    @Operation(summary = "Increase Interest Rate", description = "Increase interest rate for active personal loans (RECOVERY_MANAGER only)")
    @ApiResponse(responseCode = "200", description = "Interest rate updated")
    public ResponseEntity<String> increaseInterestRate(@RequestParam @Positive double percentage) {
        log.info("Increasing interest rate by percentage: {}", percentage);
        try {
            int updatedRows = loanRecoveryService.increaseInterestRate(percentage);
            log.info("Interest rate updated successfully for {} loans", updatedRows);
            return ResponseEntity.ok("Updated loans: " + updatedRows);
        } catch (Exception e) {
            log.error("Error increasing interest rate with percentage: {}", percentage, e);
            throw e;
        }
    }

    @GetMapping("/customers/city/{city}")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Customers by City", description = "Fetch all customers in a specific city")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<Customer>> getCustomersByCity(@PathVariable String city) {
        return ResponseEntity.ok(loanRecoveryService.findCustomersByCity(city));
    }

    @GetMapping("/loans/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Loans by Status", description = "Fetch loans filtered by status (ACTIVE, DEFAULTED, CLOSED)")
    @ApiResponse(responseCode = "200", description = "List of loans")
    public ResponseEntity<List<Loan>> getLoansByStatus(@PathVariable String status) {
        return ResponseEntity.ok(loanRecoveryService.findLoansByStatus(status));
    }

    @GetMapping("/loans/amount-greater-than")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Loans Above Amount", description = "Fetch loans with EMI amount greater than specified value")
    @ApiResponse(responseCode = "200", description = "List of loans")
    public ResponseEntity<List<Loan>> getLoansByAmount(@RequestParam @Positive double amount) {
        return ResponseEntity.ok(loanRecoveryService.findLoansByAmountGreaterThan(amount));
    }

    @GetMapping("/payments/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Payments by Status", description = "Fetch EMI payments filtered by status (PAID, PENDING, MISSED)")
    @ApiResponse(responseCode = "200", description = "List of EMI payments")
    public ResponseEntity<List<EmiPayment>> getPaymentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(loanRecoveryService.findPaymentsByStatus(status));
    }

    @GetMapping("/customers/credit-score")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Low Credit Score Customers", description = "Fetch customers with credit score below threshold")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<Customer>> getCustomersByCreditScore(@RequestParam Integer threshold) {
        return ResponseEntity.ok(loanRecoveryService.findCustomersByCreditScoreLessThan(threshold));
    }

    @GetMapping("/reports/high-risk-customers")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get High Risk Customers", description = "Identify customers with multiple missed EMI payments")
    @ApiResponse(responseCode = "200", description = "List of high-risk customers")
    public ResponseEntity<List<Customer>> getHighRiskCustomers(@RequestParam(defaultValue = "2") @Min(0) long missedThreshold) {
        return ResponseEntity.ok(loanRecoveryService.findHighRiskCustomers(missedThreshold));
    }

    @GetMapping("/reports/outstanding-city-wise")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Outstanding Amount by City", description = "Generate report of total pending EMI grouped by city")
    @ApiResponse(responseCode = "200", description = "City-wise outstanding summary")
    public ResponseEntity<List<CityOutstandingDTO>> getOutstandingCityWise() {
        return ResponseEntity.ok(loanRecoveryService.getOutstandingAmountCityWise());
    }

    @GetMapping("/reports/multiple-loan-types")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Customers with Multiple Loan Types", description = "Identify customers having more than one loan type")
    @ApiResponse(responseCode = "200", description = "List of customers")
    public ResponseEntity<List<Customer>> getMultipleLoanTypeCustomers() {
        return ResponseEntity.ok(loanRecoveryService.getCustomersWithMultipleLoanTypes());
    }

    @GetMapping("/penalties/latest")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Latest Penalty", description = "Retrieve the most recently generated penalty record")
    @ApiResponse(responseCode = "200", description = "Latest penalty record")
    public ResponseEntity<Penalty> getLatestPenalty() {
        return ResponseEntity.ok(loanRecoveryService.getLatestPenaltyGenerated());
    }

    @GetMapping("/loans/without-missed")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Get Compliant Loans", description = "Fetch all loans with zero missed EMI payments")
    @ApiResponse(responseCode = "200", description = "List of loans")
    public ResponseEntity<List<Loan>> getLoansWithoutMissedEmi() {
        return ResponseEntity.ok(loanRecoveryService.getLoansWithoutMissedEmi());
    }

    @GetMapping("/reports/top-defaulters")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Top Defaulters", description = "Get top N defaulters by total pending EMI amount")
    @ApiResponse(responseCode = "200", description = "Top defaulters list")
    public ResponseEntity<List<CustomerPendingEmiDTO>> getTopDefaulters(@RequestParam(defaultValue = "5") @Min(1) int size) {
        return ResponseEntity.ok(loanRecoveryService.getTopDefaulters(size));
    }

    @GetMapping("/reports/customer-loan-summary")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Customer Loan Summary", description = "Get consolidated report of customer loans and penalties")
    @ApiResponse(responseCode = "200", description = "Customer summary data")
    public ResponseEntity<List<CustomerLoanSummaryDTO>> getCustomerLoanSummary() {
        return ResponseEntity.ok(loanRecoveryService.getCustomerLoanSummary());
    }

    @GetMapping("/loans")
    @Operation(summary = "Get All Loans (Paginated)", description = "Fetch loans with pagination and sorting (default sorted by emiAmount DESC)")
    @ApiResponse(responseCode = "200", description = "Paginated loan list")
    public ResponseEntity<Page<Loan>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "emiAmount") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(loanRecoveryService.getLoans(pageable));
    }

    @GetMapping("/customers/me/loans")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "My Loans", description = "Get my own loans (CUSTOMER role)")
    @ApiResponse(responseCode = "200", description = "My loans list")
    public ResponseEntity<List<Loan>> getMyLoans(Authentication authentication) {
        return ResponseEntity.ok(loanRecoveryService.getLoansForLoggedInCustomer(authentication.getName()));
    }

    @GetMapping("/customers/me/emi-history")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "My EMI History", description = "Get my EMI payment history (CUSTOMER role)")
    @ApiResponse(responseCode = "200", description = "My EMI payment records")
    public ResponseEntity<List<EmiPayment>> getMyEmiHistory(Authentication authentication) {
        return ResponseEntity.ok(loanRecoveryService.getEmiHistoryForLoggedInCustomer(authentication.getName()));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','AUDITOR','RECOVERY_MANAGER')")
    @Operation(summary = "Recovery Dashboard", description = "Get comprehensive dashboard metrics and analytics")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard metrics"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    public ResponseEntity<DashboardDTO> getDashboard() {
        log.debug("Fetching dashboard metrics");
        try {
            DashboardDTO dashboard = loanRecoveryService.getDashboard();
            log.info("Dashboard metrics fetched successfully");
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            log.error("Error fetching dashboard metrics", e);
            throw e;
        }
    }
}



