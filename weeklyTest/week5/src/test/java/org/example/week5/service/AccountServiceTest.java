package org.example.week5.service;

import org.example.week5.dto.AccountDTO;
import org.example.week5.entity.Account;
import org.example.week5.entity.Customer;
import org.example.week5.entity.Transaction;
import org.example.week5.exception.AccountNotFoundException;
import org.example.week5.exception.CustomerNotFoundException;
import org.example.week5.exception.InsufficientBalanceException;
import org.example.week5.exception.InvalidTransferException;
import org.example.week5.repository.AccountRepository;
import org.example.week5.repository.CustomerRepository;
import org.example.week5.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Account Service Tests")
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private AccountDTO testAccountDTO;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john@example.com");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123456");
        testAccount.setAccountType(Account.AccountType.SAVINGS);
        testAccount.setBalance(new BigDecimal("10000.00"));
        testAccount.setCustomer(testCustomer);

        testAccountDTO = new AccountDTO();
        testAccountDTO.setAccountNumber("ACC123456");
        testAccountDTO.setAccountType("SAVINGS");
        testAccountDTO.setBalance(new BigDecimal("10000.00"));
        testAccountDTO.setCustomerId(1L);
    }

    @Test
    @DisplayName("Should create account successfully with valid data")
    void testCreateAccount_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // Act
        AccountDTO result = accountService.createAccount(testAccountDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ACC123456", result.getAccountNumber());
        assertEquals("SAVINGS", result.getAccountType());
        verify(customerRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when creating account for non-existent customer")
    void testCreateAccount_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setCustomerId(99L);
        accountDTO.setAccountNumber("ACC123456");
        accountDTO.setAccountType("SAVINGS");
        accountDTO.setBalance(new BigDecimal("10000.00"));

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            accountService.createAccount(accountDTO);
        });
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when opening balance is negative")
    void testCreateAccount_NegativeBalance() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        AccountDTO negativeBalanceDTO = new AccountDTO();
        negativeBalanceDTO.setCustomerId(1L);
        negativeBalanceDTO.setAccountNumber("ACC123456");
        negativeBalanceDTO.setAccountType("SAVINGS");
        negativeBalanceDTO.setBalance(new BigDecimal("-1000.00"));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.createAccount(negativeBalanceDTO);
        });
    }

    @Test
    @DisplayName("Should throw exception when account number is duplicate")
    void testCreateAccount_DuplicateAccountNumber() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(accountRepository.existsByAccountNumber("ACC123456")).thenReturn(true);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.createAccount(testAccountDTO);
        });
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate account type is either SAVINGS or CURRENT")
    void testCreateAccount_InvalidAccountType() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        AccountDTO invalidTypeDTO = new AccountDTO();
        invalidTypeDTO.setCustomerId(1L);
        invalidTypeDTO.setAccountNumber("ACC123456");
        invalidTypeDTO.setAccountType("INVALID");
        invalidTypeDTO.setBalance(new BigDecimal("10000.00"));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.createAccount(invalidTypeDTO);
        });
    }

    @Test
    @DisplayName("Should retrieve all accounts")
    void testGetAllAccounts() {
        // Arrange
        Account account2 = new Account();
        account2.setId(2L);
        account2.setAccountNumber("ACC789012");
        account2.setAccountType(Account.AccountType.CURRENT);
        account2.setBalance(new BigDecimal("5000.00"));

        when(accountRepository.findAll()).thenReturn(Arrays.asList(testAccount, account2));

        // Act
        List<AccountDTO> result = accountService.getAllAccounts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve account by id")
    void testGetAccountById_Success() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        AccountDTO result = accountService.getAccountById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("ACC123456", result.getAccountNumber());
        verify(accountRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when account not found by id")
    void testGetAccountById_NotFound() {
        // Arrange
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(99L);
        });
    }

    @Test
    @DisplayName("Should update account successfully")
    void testUpdateAccount_Success() {
        // Arrange
        AccountDTO updateDTO = new AccountDTO();
        updateDTO.setAccountType("CURRENT");
        updateDTO.setBalance(new BigDecimal("15000.00"));

        Account updatedAccount = new Account();
        updatedAccount.setId(1L);
        updatedAccount.setAccountNumber("ACC123456");
        updatedAccount.setAccountType(Account.AccountType.CURRENT);
        updatedAccount.setBalance(new BigDecimal("15000.00"));
        updatedAccount.setCustomer(testCustomer);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        AccountDTO result = accountService.updateAccount(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("CURRENT", result.getAccountType());
        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should delete account successfully")
    void testDeleteAccount_Success() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        accountService.deleteAccount(1L);

        // Assert
        verify(accountRepository).findById(1L);
        verify(accountRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should deposit amount successfully and increase balance")
    void testDeposit_Success() {
        // Arrange
        BigDecimal depositAmount = new BigDecimal("5000.00");
        BigDecimal expectedBalance = testAccount.getBalance().add(depositAmount);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        accountService.deposit(1L, depositAmount, "Salary deposit");

        // Assert
        assertEquals(expectedBalance, testAccount.getBalance());
        verify(accountRepository).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when deposit amount is zero")
    void testDeposit_ZeroAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.deposit(1L, BigDecimal.ZERO, "Deposit");
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when deposit amount is negative")
    void testDeposit_NegativeAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.deposit(1L, new BigDecimal("-1000.00"), "Deposit");
        });
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when depositing to non-existent account")
    void testDeposit_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.deposit(99L, new BigDecimal("1000.00"), "Deposit");
        });
    }

    @Test
    @DisplayName("Should withdraw amount successfully and decrease balance")
    void testWithdraw_Success() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("2000.00");
        BigDecimal expectedBalance = testAccount.getBalance().subtract(withdrawAmount);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        accountService.withdraw(1L, withdrawAmount, "Withdrawal");

        // Assert
        assertEquals(expectedBalance, testAccount.getBalance());
        verify(accountRepository).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when withdrawal amount exceeds balance")
    void testWithdraw_InsufficientBalance() {
        // Arrange
        BigDecimal withdrawAmount = new BigDecimal("20000.00");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(1L, withdrawAmount, "Withdrawal");
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when withdrawal amount is zero")
    void testWithdraw_ZeroAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.withdraw(1L, BigDecimal.ZERO, "Withdrawal");
        });
    }

    @Test
    @DisplayName("Should throw exception when withdrawal amount is negative")
    void testWithdraw_NegativeAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.withdraw(1L, new BigDecimal("-1000.00"), "Withdrawal");
        });
    }

    @Test
    @DisplayName("Should transfer amount successfully between two accounts")
    void testTransfer_Success() {
        // Arrange
        Account sourceAccount = new Account();
        sourceAccount.setId(1L);
        sourceAccount.setBalance(new BigDecimal("10000.00"));
        sourceAccount.setCustomer(testCustomer);

        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("5000.00"));
        destinationAccount.setCustomer(testCustomer);

        BigDecimal transferAmount = new BigDecimal("2000.00");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(sourceAccount);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        // Act
        accountService.transfer(1L, 2L, transferAmount, "Transfer");

        // Assert
        assertEquals(new BigDecimal("8000.00"), sourceAccount.getBalance());
        assertEquals(new BigDecimal("7000.00"), destinationAccount.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when transferring to same account")
    void testTransfer_SameAccount() {
        // Arrange
        BigDecimal transferAmount = new BigDecimal("1000.00");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(InvalidTransferException.class, () -> {
            accountService.transfer(1L, 1L, transferAmount, "Transfer");
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when transferring with insufficient balance")
    void testTransfer_InsufficientBalance() {
        // Arrange
        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("5000.00"));

        BigDecimal transferAmount = new BigDecimal("20000.00");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.transfer(1L, 2L, transferAmount, "Transfer");
        });
        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when source account not found")
    void testTransfer_SourceAccountNotFound() {
        // Arrange
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(99L, 1L, new BigDecimal("1000.00"), "Transfer");
        });
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when destination account not found")
    void testTransfer_DestinationAccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.transfer(1L, 99L, new BigDecimal("1000.00"), "Transfer");
        });
    }

    @Test
    @DisplayName("Should throw exception when transfer amount is zero or negative")
    void testTransfer_InvalidAmount() {
        // Arrange
        Account destinationAccount = new Account();
        destinationAccount.setId(2L);
        destinationAccount.setBalance(new BigDecimal("5000.00"));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            accountService.transfer(1L, 2L, BigDecimal.ZERO, "Transfer");
        });
    }
}

