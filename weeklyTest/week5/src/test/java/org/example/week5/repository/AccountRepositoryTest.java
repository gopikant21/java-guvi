package org.example.week5.repository;

import org.example.week5.entity.Account;
import org.example.week5.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Account Repository Tests")
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Account testAccount;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPhone("9876543210");
        testCustomer = customerRepository.save(testCustomer);

        testAccount = new Account();
        testAccount.setAccountNumber("ACC123456");
        testAccount.setAccountType(Account.AccountType.SAVINGS);
        testAccount.setBalance(new BigDecimal("10000.00"));
        testAccount.setCustomer(testCustomer);
    }

    @Test
    @DisplayName("Should save account successfully")
    void testSaveAccount() {
        // Act
        Account savedAccount = accountRepository.save(testAccount);

        // Assert
        assertNotNull(savedAccount.getId());
        assertEquals("ACC123456", savedAccount.getAccountNumber());
        assertEquals(Account.AccountType.SAVINGS, savedAccount.getAccountType());
    }

    @Test
    @DisplayName("Should find account by id")
    void testFindById() {
        // Arrange
        Account savedAccount = accountRepository.save(testAccount);

        // Act
        Optional<Account> foundAccount = accountRepository.findById(savedAccount.getId());

        // Assert
        assertTrue(foundAccount.isPresent());
        assertEquals("ACC123456", foundAccount.get().getAccountNumber());
    }

    @Test
    @DisplayName("Should find account by account number")
    void testFindByAccountNumber() {
        // Arrange
        accountRepository.save(testAccount);

        // Act
        Optional<Account> foundAccount = accountRepository.findByAccountNumber("ACC123456");

        // Assert
        assertTrue(foundAccount.isPresent());
        assertEquals("ACC123456", foundAccount.get().getAccountNumber());
    }

    @Test
    @DisplayName("Should return empty when account number not found")
    void testFindByAccountNumber_NotFound() {
        // Act
        Optional<Account> foundAccount = accountRepository.findByAccountNumber("NONEXISTENT");

        // Assert
        assertFalse(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Should check if account number exists")
    void testExistsByAccountNumber() {
        // Arrange
        accountRepository.save(testAccount);

        // Act
        boolean exists = accountRepository.existsByAccountNumber("ACC123456");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when account number does not exist")
    void testExistsByAccountNumber_NotExists() {
        // Act
        boolean exists = accountRepository.existsByAccountNumber("NONEXISTENT");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should find all accounts by customer id")
    void testFindByCustomerId() {
        // Arrange
        accountRepository.save(testAccount);

        Account account2 = new Account();
        account2.setAccountNumber("ACC789012");
        account2.setAccountType(Account.AccountType.CURRENT);
        account2.setBalance(new BigDecimal("5000.00"));
        account2.setCustomer(testCustomer);
        accountRepository.save(account2);

        // Act
        List<Account> accounts = accountRepository.findByCustomerId(testCustomer.getId());

        // Assert
        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.getAccountNumber().equals("ACC123456")));
        assertTrue(accounts.stream().anyMatch(a -> a.getAccountNumber().equals("ACC789012")));
    }

    @Test
    @DisplayName("Should return empty list when customer has no accounts")
    void testFindByCustomerId_NoAccounts() {
        // Act
        List<Account> accounts = accountRepository.findByCustomerId(testCustomer.getId());

        // Assert
        assertEquals(0, accounts.size());
    }

    @Test
    @DisplayName("Should update account")
    void testUpdateAccount() {
        // Arrange
        Account savedAccount = accountRepository.save(testAccount);
        savedAccount.setBalance(new BigDecimal("15000.00"));

        // Act
        Account updatedAccount = accountRepository.save(savedAccount);

        // Assert
        assertEquals(new BigDecimal("15000.00"), updatedAccount.getBalance());
    }

    @Test
    @DisplayName("Should delete account")
    void testDeleteAccount() {
        // Arrange
        Account savedAccount = accountRepository.save(testAccount);
        Long accountId = savedAccount.getId();

        // Act
        accountRepository.deleteById(accountId);

        // Assert
        assertFalse(accountRepository.findById(accountId).isPresent());
    }

    @Test
    @DisplayName("Should enforce account number uniqueness constraint")
    void testAccountNumberUniquenessConstraint() {
        // Arrange
        accountRepository.save(testAccount);

        Account duplicateAccount = new Account();
        duplicateAccount.setAccountNumber("ACC123456");
        duplicateAccount.setAccountType(Account.AccountType.CURRENT);
        duplicateAccount.setBalance(new BigDecimal("5000.00"));
        duplicateAccount.setCustomer(testCustomer);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountRepository.save(duplicateAccount);
            accountRepository.flush();
        });
    }

    @Test
    @DisplayName("Should persist account with SAVINGS type")
    void testSaveAccount_SavingsType() {
        // Act
        Account savedAccount = accountRepository.save(testAccount);

        // Assert
        assertEquals(Account.AccountType.SAVINGS, savedAccount.getAccountType());
    }

    @Test
    @DisplayName("Should persist account with CURRENT type")
    void testSaveAccount_CurrentType() {
        // Arrange
        testAccount.setAccountType(Account.AccountType.CURRENT);

        // Act
        Account savedAccount = accountRepository.save(testAccount);

        // Assert
        assertEquals(Account.AccountType.CURRENT, savedAccount.getAccountType());
    }

    @Test
    @DisplayName("Should persist zero balance")
    void testSaveAccount_ZeroBalance() {
        // Arrange
        testAccount.setBalance(BigDecimal.ZERO);

        // Act
        Account savedAccount = accountRepository.save(testAccount);

        // Assert
        assertEquals(BigDecimal.ZERO, savedAccount.getBalance());
    }

    @Test
    @DisplayName("Should count total accounts")
    void testCountAccounts() {
        // Arrange
        accountRepository.save(testAccount);

        Account account2 = new Account();
        account2.setAccountNumber("ACC789012");
        account2.setAccountType(Account.AccountType.CURRENT);
        account2.setBalance(new BigDecimal("5000.00"));
        account2.setCustomer(testCustomer);
        accountRepository.save(account2);

        // Act
        long count = accountRepository.count();

        // Assert
        assertEquals(2, count);
    }
}

