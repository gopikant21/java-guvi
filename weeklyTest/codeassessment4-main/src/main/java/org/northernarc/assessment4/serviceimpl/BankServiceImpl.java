package org.northernarc.assessment4.serviceimpl;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.exception.AccountNotFoundException;
import org.northernarc.assessment4.exception.CustomerNotFoundException;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.northernarc.assessment4.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankServiceImpl implements BankService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // --- Core Entity Writing Persistence Methods ---
    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        accountRepository.delete(account);
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
    }

    // --- Task 3: Spring Data JPA Derived Query Methods ---
    @Override
    public List<Account> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    @Override
    public List<Customer> getCustomersByBranch(String branch) {
        return customerRepository.findByBranch(branch);
    }

    @Override
    public List<Transaction> getTransactionsByType(String transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<Account> getAccountsWithBalanceGreaterThan(double amount) {
        return accountRepository.findByBalanceGreaterThan(amount);
    }

    // --- Task 4: JPQL Custom Queries ---
    @Override
    public List<Customer> getRichCustomers(double threshold) {
        return customerRepository.findRichCustomersAlt(threshold);
    }

    @Override
    public Map<String, Double> getTotalBalancePerBranch() {
        List<Object[]> results = customerRepository.findTotalBalancePerBranch();
        Map<String, Double> branchBalances = new HashMap<>();

        for (Object[] result : results) {
            String branch = (String) result[0];
            Double totalBalance = ((Number) result[1]).doubleValue();
            branchBalances.put(branch, totalBalance);
        }

        return branchBalances;
    }

    @Override
    public List<Customer> getCustomersWithMultipleAccounts() {
        return customerRepository.findCustomersWithMultipleAccounts();
    }

    @Override
    public Transaction getLatestTransaction() {
        return transactionRepository.findTopByOrderByTransactionDateDesc()
                .orElseThrow(() -> new RuntimeException("No transactions found"));
    }

    @Override
    public List<Account> getAccountsWithNoTransactions() {
        return accountRepository.findAccountsWithNoTransactions();
    }

    // --- Task 5: JPQL Update Query using @Modifying and @Transactional ---
    @Override
    @Transactional
    public void increaseAccountBalance(String accountNumber, double amount) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        accountRepository.increaseBalance(accountNumber, amount);
    }

    // --- Task 6: Pagination & Sorting ---
    @Override
    public Page<Account> getAllAccountsPaginated(Pageable pageable) {
        return accountRepository.findAllAccounts(pageable);
    }

    // --- Task 7: DTO Projection Mapping Layer ---
    @Override
    public CustomerSummaryDTO getCustomerSummary(Long customerId) {
        return customerRepository.findCustomerSummary(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }

    // --- Final Challenge (40 Marks): Dashboard Metrics - Optimized Single Query Strategy ---
    @Override
    public DashboardResponse getDashboardMetrics() {
        Long totalCustomers = customerRepository.count();
        Long totalAccounts = accountRepository.count();

        Double totalBalance = accountRepository.getTotalBalance();

        String topBranch = customerRepository.findTopBranchByBalance(PageRequest.of(0, 1)).stream()
                .findFirst()
                .orElse("N/A");

        String highestBalanceCustomer = customerRepository.findCustomersByHighestBalance(PageRequest.of(0, 1)).stream()
                .findFirst()
                .orElse("N/A");

        return new DashboardResponse(
                totalCustomers,
                totalAccounts,
                totalBalance,
                topBranch,
                highestBalanceCustomer
        );
    }
}
