package org.northernarc.assessment4.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.security.JwtUtil;
import org.northernarc.assessment4.service.BankService;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BankController - REST API endpoints for banking operations
 * Includes endpoints for customer management, account operations, transactions, and metrics
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // --- Task 8: JWT Authentication Endpoint ---
    /**
     * Login endpoint - authenticates user and returns JWT token
     * POST /api/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email and password are required"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("message", "Login successful");

            log.info("User {} logged in successfully", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed: Invalid credentials"));
        }
    }

    // --- Core Onboarding & Setup Endpoints ---
    /**
     * Create a new customer
     * POST /api/customers
     */
    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            // Encode password before saving
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            Customer savedCustomer = bankService.saveCustomer(customer);
            log.info("New customer created with ID: {}", savedCustomer.getCustomerId());
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating customer: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Create a new account
     * POST /api/accounts
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        Account savedAccount = bankService.saveAccount(account);
        log.info("New account created: {}", savedAccount.getAccountNumber());
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    // --- Task 6: Pagination & Sorting (Default: Balance DESC) ---
    /**
     * Get all accounts with pagination and sorting by balance DESC
     * GET /api/accounts?page=0&size=10
     */
    @GetMapping("/accounts")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<Account>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "balance") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Account> accounts = bankService.getAllAccountsPaginated(pageable);

        log.info("Fetched {} accounts with pagination", accounts.getSize());
        return ResponseEntity.ok(accounts);
    }

    // --- Task 9: Role Based Access Control Endpoints ---
    /**
     * Delete an account - Only ADMIN can delete
     * DELETE /api/accounts/{accountNumber}
     */
    @DeleteMapping("/accounts/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        bankService.deleteAccount(accountNumber);
        log.info("Account deleted: {}", accountNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update account balance - Only MANAGER can update
     * PUT /api/accounts/{accountNumber}/balance
     */
    @PutMapping("/accounts/{accountNumber}/balance")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> updateAccountBalance(
            @PathVariable String accountNumber,
            @RequestParam double amount) {

        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        bankService.increaseAccountBalance(accountNumber, amount);
        log.info("Account {} balance increased by {}", accountNumber, amount);
        return ResponseEntity.ok().build();
    }

    /**
     * View account by account number - Only USER can view
     * GET /api/accounts/view/{accountNumber}
     */
    @GetMapping("/accounts/view/{accountNumber}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Account> viewAccountByNumber(@PathVariable String accountNumber) {
        Account account = bankService.getAccountByNumber(accountNumber);
        log.info("Retrieved account by number: {}", accountNumber);
        return ResponseEntity.ok(account);
    }

    // --- Task 7: DTO Projection Aggregation ---
    /**
     * Get customer summary DTO with aggregated account information
     * GET /api/customers/{customerId}/summary
     */
    @GetMapping("/customers/{customerId}/summary")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<CustomerSummaryDTO> getCustomerSummary(@PathVariable Long customerId) {
        CustomerSummaryDTO summary = bankService.getCustomerSummary(customerId);
        log.info("Retrieved summary for customer: {}", customerId);
        return ResponseEntity.ok(summary);
    }

    // --- Task 3 & 4: Query Operations ---
    /**
     * Get customers with total balance exceeding threshold
     * GET /api/customers/rich?threshold=100000
     */
    @GetMapping("/customers/rich")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<Customer>> getRichCustomers(@RequestParam double threshold) {
        List<Customer> richCustomers = bankService.getRichCustomers(threshold);
        log.info("Found {} rich customers with threshold: {}", richCustomers.size(), threshold);
        return ResponseEntity.ok(richCustomers);
    }

    /**
     * Get total balance per branch
     * GET /api/branches/balances
     */
    @GetMapping("/branches/balances")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Map<String, Double>> getBranchBalances() {
        Map<String, Double> branchBalances = bankService.getTotalBalancePerBranch();
        log.info("Retrieved balance information for {} branches", branchBalances.size());
        return ResponseEntity.ok(branchBalances);
    }

    /**
     * Get customers having multiple accounts
     * GET /api/customers/multiple-accounts
     */
    @GetMapping("/customers/multiple-accounts")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<Customer>> getCustomersWithMultipleAccounts() {
        List<Customer> customers = bankService.getCustomersWithMultipleAccounts();
        log.info("Found {} customers with multiple accounts", customers.size());
        return ResponseEntity.ok(customers);
    }

    /**
     * Get latest transaction
     * GET /api/transactions/latest
     */
    @GetMapping("/transactions/latest")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Transaction> getLatestTransaction() {
        Transaction transaction = bankService.getLatestTransaction();
        log.info("Retrieved latest transaction: {}", transaction.getTransactionId());
        return ResponseEntity.ok(transaction);
    }

    /**
     * Get accounts with no transactions (idle accounts)
     * GET /api/accounts/idle
     */
    @GetMapping("/accounts/idle")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<Account>> getIdleAccounts() {
        List<Account> idleAccounts = bankService.getAccountsWithNoTransactions();
        log.info("Found {} idle accounts with no transactions", idleAccounts.size());
        return ResponseEntity.ok(idleAccounts);
    }

    // --- Final Challenge: Metrics Dashboard Generation ---
    /**
     * Get comprehensive dashboard metrics
     * GET /api/dashboard
     * Returns: totalCustomers, totalAccounts, totalBalance, topBranch, highestBalanceCustomer
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<DashboardResponse> getDashboardMetrics() {
        DashboardResponse dashboard = bankService.getDashboardMetrics();
        log.info("Dashboard metrics retrieved - Customers: {}, Accounts: {}, Total Balance: {}",
                dashboard.totalCustomers(), dashboard.totalAccounts(), dashboard.totalBalance());
        return ResponseEntity.ok(dashboard);
    }
}
