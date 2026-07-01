package org.northernarc.assessment4.config;

import lombok.RequiredArgsConstructor;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Database initialization with sample data for testing
 * This configuration runs automatically on application startup
 */
@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeDatabase(
            CustomerRepository customerRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        return args -> {
            // Skip initialization if data already exists
            if (customerRepository.count() > 0) {
                return;
            }

            // Create sample customers
            Customer customer1 = new Customer();
            customer1.setCustomerName("Rahul Sharma");
            customer1.setEmail("rahul.sharma@bank.com");
            customer1.setPassword(passwordEncoder.encode("password123"));
            customer1.setBranch("Chennai");

            Customer customer2 = new Customer();
            customer2.setCustomerName("Priya Patel");
            customer2.setEmail("priya.patel@bank.com");
            customer2.setPassword(passwordEncoder.encode("password123"));
            customer2.setBranch("Mumbai");

            Customer customer3 = new Customer();
            customer3.setCustomerName("Amit Kumar");
            customer3.setEmail("amit.kumar@bank.com");
            customer3.setPassword(passwordEncoder.encode("password123"));
            customer3.setBranch("Bangalore");

            customer1 = customerRepository.save(customer1);
            customer2 = customerRepository.save(customer2);
            customer3 = customerRepository.save(customer3);

            // Create sample accounts
            Account account1 = new Account();
            account1.setAccountNumber("ACC001");
            account1.setAccountType("SAVINGS");
            account1.setBalance(50000.0);
            account1.setCustomer(customer1);

            Account account2 = new Account();
            account2.setAccountNumber("ACC002");
            account2.setAccountType("CURRENT");
            account2.setBalance(100000.0);
            account2.setCustomer(customer1);

            Account account3 = new Account();
            account3.setAccountNumber("ACC003");
            account3.setAccountType("SAVINGS");
            account3.setBalance(75000.0);
            account3.setCustomer(customer2);

            Account account4 = new Account();
            account4.setAccountNumber("ACC004");
            account4.setAccountType("CURRENT");
            account4.setBalance(200000.0);
            account4.setCustomer(customer3);

            account1 = accountRepository.save(account1);
            account2 = accountRepository.save(account2);
            account3 = accountRepository.save(account3);
            account4 = accountRepository.save(account4);

            // Create sample transactions
            Transaction transaction1 = new Transaction();
            transaction1.setAccount(account1);
            transaction1.setAmount(5000.0);
            transaction1.setTransactionType("DEPOSIT");
            transaction1.setTransactionDate(LocalDate.now().minusDays(10));
            transactionRepository.save(transaction1);

            Transaction transaction2 = new Transaction();
            transaction2.setAccount(account2);
            transaction2.setAmount(2000.0);
            transaction2.setTransactionType("WITHDRAWAL");
            transaction2.setTransactionDate(LocalDate.now().minusDays(5));
            transactionRepository.save(transaction2);

            Transaction transaction3 = new Transaction();
            transaction3.setAccount(account3);
            transaction3.setAmount(10000.0);
            transaction3.setTransactionType("TRANSFER");
            transaction3.setTransactionDate(LocalDate.now().minusDays(2));
            transactionRepository.save(transaction3);

            Transaction transaction4 = new Transaction();
            transaction4.setAccount(account4);
            transaction4.setAmount(15000.0);
            transaction4.setTransactionType("DEPOSIT");
            transaction4.setTransactionDate(LocalDate.now());
            transactionRepository.save(transaction4);

            System.out.println("✓ Database initialized with sample data");
            System.out.println("✓ Created 3 customers, 4 accounts, and 4 transactions");
        };
    }
}

