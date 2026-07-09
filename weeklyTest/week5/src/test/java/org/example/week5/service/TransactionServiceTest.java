package org.example.week5.service;

import org.example.week5.dto.TransactionDTO;
import org.example.week5.entity.Account;
import org.example.week5.entity.Customer;
import org.example.week5.entity.Transaction;
import org.example.week5.exception.AccountNotFoundException;
import org.example.week5.repository.AccountRepository;
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
@DisplayName("Transaction Service Tests")
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction testTransaction;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        Customer testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAccountNumber("ACC123456");
        testAccount.setBalance(new BigDecimal("10000.00"));
        testAccount.setCustomer(testCustomer);

        testTransaction = new Transaction();
        testTransaction.setId(1L);
        testTransaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        testTransaction.setAmount(new BigDecimal("1000.00"));
        testTransaction.setTransactionDate(LocalDateTime.now());
        testTransaction.setDescription("Salary deposit");
        testTransaction.setAccount(testAccount);
    }

    @Test
    @DisplayName("Should retrieve all transactions")
    void testGetAllTransactions() {
        // Arrange
        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction2.setAmount(new BigDecimal("500.00"));

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction, transaction2));

        // Act
        List<TransactionDTO> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve transaction by id")
    void testGetTransactionById_Success() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(testTransaction));

        // Act
        TransactionDTO result = transactionService.getTransactionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("DEPOSIT", result.getTransactionType());
        assertEquals(new BigDecimal("1000.00"), result.getAmount());
        verify(transactionRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when transaction not found by id")
    void testGetTransactionById_NotFound() {
        // Arrange
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> {
            transactionService.getTransactionById(99L);
        });
    }

    @Test
    @DisplayName("Should retrieve all transactions for a specific account")
    void testGetTransactionsByAccountId_Success() {
        // Arrange
        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        transaction2.setAmount(new BigDecimal("500.00"));
        transaction2.setAccount(testAccount);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(testTransaction, transaction2));

        // Act
        List<TransactionDTO> result = transactionService.getTransactionsByAccountId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionRepository).findByAccountId(1L);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when retrieving transactions for non-existent account")
    void testGetTransactionsByAccountId_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getTransactionsByAccountId(99L);
        });
        verify(transactionRepository, never()).findByAccountId(anyLong());
    }

    @Test
    @DisplayName("Should retrieve empty list when account has no transactions")
    void testGetTransactionsByAccountId_EmptyList() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList());

        // Act
        List<TransactionDTO> result = transactionService.getTransactionsByAccountId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should verify deposit transaction is recorded")
    void testDepositTransaction_Recorded() {
        // Arrange
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        TransactionDTO result = transactionService.recordTransaction(
            1L, Transaction.TransactionType.DEPOSIT, new BigDecimal("1000.00"), "Deposit");

        // Assert
        assertNotNull(result);
        assertEquals("DEPOSIT", result.getTransactionType());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should verify withdrawal transaction is recorded")
    void testWithdrawalTransaction_Recorded() {
        // Arrange
        Transaction withdrawalTransaction = new Transaction();
        withdrawalTransaction.setId(2L);
        withdrawalTransaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
        withdrawalTransaction.setAmount(new BigDecimal("500.00"));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(withdrawalTransaction);

        // Act
        TransactionDTO result = transactionService.recordTransaction(
            1L, Transaction.TransactionType.WITHDRAWAL, new BigDecimal("500.00"), "Withdrawal");

        // Assert
        assertNotNull(result);
        assertEquals("WITHDRAWAL", result.getTransactionType());
    }

    @Test
    @DisplayName("Should verify transfer transaction is recorded")
    void testTransferTransaction_Recorded() {
        // Arrange
        Transaction transferTransaction = new Transaction();
        transferTransaction.setId(3L);
        transferTransaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transferTransaction.setAmount(new BigDecimal("2000.00"));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transferTransaction);

        // Act
        TransactionDTO result = transactionService.recordTransaction(
            1L, Transaction.TransactionType.TRANSFER, new BigDecimal("2000.00"), "Transfer");

        // Assert
        assertNotNull(result);
        assertEquals("TRANSFER", result.getTransactionType());
    }

    @Test
    @DisplayName("Should record transaction with correct timestamp")
    void testRecordTransaction_TimestampIsSet() {
        // Arrange
        LocalDateTime beforeCreation = LocalDateTime.now();
        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setTransactionDate(LocalDateTime.now());
        savedTransaction.setAmount(new BigDecimal("1000.00"));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // Act
        TransactionDTO result = transactionService.recordTransaction(
            1L, Transaction.TransactionType.DEPOSIT, new BigDecimal("1000.00"), "Deposit");

        // Assert
        assertNotNull(result.getTransactionDate());
        assertTrue(result.getTransactionDate().isAfter(beforeCreation) ||
                  result.getTransactionDate().isEqual(beforeCreation));
    }

    @Test
    @DisplayName("Should throw exception when amount is zero")
    void testRecordTransaction_ZeroAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            transactionService.recordTransaction(1L, Transaction.TransactionType.DEPOSIT,
                BigDecimal.ZERO, "Deposit");
        });
    }

    @Test
    @DisplayName("Should throw exception when amount is negative")
    void testRecordTransaction_NegativeAmount() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            transactionService.recordTransaction(1L, Transaction.TransactionType.WITHDRAWAL,
                new BigDecimal("-1000.00"), "Withdrawal");
        });
    }

    @Test
    @DisplayName("Should record transaction with description")
    void testRecordTransaction_WithDescription() {
        // Arrange
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // Act
        TransactionDTO result = transactionService.recordTransaction(
            1L, Transaction.TransactionType.DEPOSIT, new BigDecimal("1000.00"), "Monthly salary");

        // Assert
        assertNotNull(result);
        assertEquals("Monthly salary", result.getDescription());
    }
}

