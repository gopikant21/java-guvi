package org.example.week5.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.AccountDTO;
import org.example.week5.dto.CustomerDTO;
import org.example.week5.dto.TransferRequest;
import org.example.week5.entity.Account;
import org.example.week5.entity.Customer;
import org.example.week5.entity.Transaction;
import org.example.week5.repository.AccountRepository;
import org.example.week5.repository.CustomerRepository;
import org.example.week5.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Transfer Integration Tests")
@Transactional
class TransferIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer sourceCustomer;
    private Customer destinationCustomer;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        // Clean up
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        // Create source customer and account
        sourceCustomer = new Customer();
        sourceCustomer.setName("Source Customer");
        sourceCustomer.setEmail("source@example.com");
        sourceCustomer.setPhone("1111111111");
        sourceCustomer = customerRepository.save(sourceCustomer);

        sourceAccount = new Account();
        sourceAccount.setAccountNumber("ACC001");
        sourceAccount.setAccountType(Account.AccountType.SAVINGS);
        sourceAccount.setBalance(new BigDecimal("10000.00"));
        sourceAccount.setCustomer(sourceCustomer);
        sourceAccount = accountRepository.save(sourceAccount);

        // Create destination customer and account
        destinationCustomer = new Customer();
        destinationCustomer.setName("Destination Customer");
        destinationCustomer.setEmail("destination@example.com");
        destinationCustomer.setPhone("2222222222");
        destinationCustomer = customerRepository.save(destinationCustomer);

        destinationAccount = new Account();
        destinationAccount.setAccountNumber("ACC002");
        destinationAccount.setAccountType(Account.AccountType.SAVINGS);
        destinationAccount.setBalance(new BigDecimal("5000.00"));
        destinationAccount.setCustomer(destinationCustomer);
        destinationAccount = accountRepository.save(destinationAccount);
    }

    @Test
    @DisplayName("Should transfer amount successfully between accounts")
    void testTransfer_Success() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("2000.00"),
            "Test Transfer"
        );

        // Act
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());

        // Assert - Check balances
        Account updatedSourceAccount = accountRepository.findById(sourceAccount.getId()).orElse(null);
        Account updatedDestinationAccount = accountRepository.findById(destinationAccount.getId()).orElse(null);

        assert updatedSourceAccount != null;
        assert updatedDestinationAccount != null;
        assertEquals(new BigDecimal("8000.00"), updatedSourceAccount.getBalance());
        assertEquals(new BigDecimal("7000.00"), updatedDestinationAccount.getBalance());
    }

    @Test
    @DisplayName("Should create two transaction records during transfer")
    void testTransfer_CreatesTransactionRecords() throws Exception {
        // Arrange
        int initialTransactionCount = (int) transactionRepository.count();
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            "Test Transfer"
        );

        // Act
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());

        // Assert - Verify two transactions created
        List<Transaction> sourceTransactions = transactionRepository.findByAccountId(sourceAccount.getId());
        List<Transaction> destTransactions = transactionRepository.findByAccountId(destinationAccount.getId());

        assertEquals(1, sourceTransactions.size());
        assertEquals(1, destTransactions.size());
        assertEquals(Transaction.TransactionType.TRANSFER, sourceTransactions.get(0).getTransactionType());
        assertEquals(Transaction.TransactionType.TRANSFER, destTransactions.get(0).getTransactionType());
    }

    @Test
    @DisplayName("Should prevent transfer to same account")
    void testTransfer_SameAccount_Rejected() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            sourceAccount.getId(), // Same account
            new BigDecimal("1000.00"),
            "Invalid Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());

        // Verify balances unchanged
        Account unchanged = accountRepository.findById(sourceAccount.getId()).orElse(null);
        assert unchanged != null;
        assertEquals(new BigDecimal("10000.00"), unchanged.getBalance());
    }

    @Test
    @DisplayName("Should prevent transfer with insufficient balance")
    void testTransfer_InsufficientBalance_Rejected() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("50000.00"), // Amount exceeds balance
            "Insufficient Balance Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());

        // Verify balances unchanged
        Account sourceCheck = accountRepository.findById(sourceAccount.getId()).orElse(null);
        Account destCheck = accountRepository.findById(destinationAccount.getId()).orElse(null);

        assert sourceCheck != null;
        assert destCheck != null;
        assertEquals(new BigDecimal("10000.00"), sourceCheck.getBalance());
        assertEquals(new BigDecimal("5000.00"), destCheck.getBalance());
    }

    @Test
    @DisplayName("Should prevent transfer with non-existent source account")
    void testTransfer_InvalidSourceAccount() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            99999L, // Non-existent
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            "Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should prevent transfer with non-existent destination account")
    void testTransfer_InvalidDestinationAccount() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            99999L, // Non-existent
            new BigDecimal("1000.00"),
            "Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should ensure transfer is atomic (all or nothing)")
    void testTransfer_IsAtomic() throws Exception {
        // This test verifies that transfer either succeeds completely or fails completely
        // Arrange
        BigDecimal sourceBalanceBefore = sourceAccount.getBalance();
        BigDecimal destBalanceBefore = destinationAccount.getBalance();

        TransferRequest validTransfer = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            "Valid Transfer"
        );

        // Act
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validTransfer)))
                .andExpect(status().isOk());

        // Assert - Verify atomicity
        Account finalSource = accountRepository.findById(sourceAccount.getId()).orElse(null);
        Account finalDest = accountRepository.findById(destinationAccount.getId()).orElse(null);

        assert finalSource != null;
        assert finalDest != null;

        // Either both succeed or both fail
        BigDecimal transferAmount = new BigDecimal("1000.00");
        assertEquals(sourceBalanceBefore.subtract(transferAmount), finalSource.getBalance());
        assertEquals(destBalanceBefore.add(transferAmount), finalDest.getBalance());
    }

    @Test
    @DisplayName("Should allow multiple sequential transfers")
    void testMultipleTransfers_Sequential() throws Exception {
        // Transfer 1
        TransferRequest transfer1 = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            "Transfer 1"
        );

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer1)))
                .andExpect(status().isOk());

        // Transfer 2
        TransferRequest transfer2 = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("2000.00"),
            "Transfer 2"
        );

        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfer2)))
                .andExpect(status().isOk());

        // Verify final balances
        Account finalSource = accountRepository.findById(sourceAccount.getId()).orElse(null);
        Account finalDest = accountRepository.findById(destinationAccount.getId()).orElse(null);

        assert finalSource != null;
        assert finalDest != null;
        assertEquals(new BigDecimal("7000.00"), finalSource.getBalance());
        assertEquals(new BigDecimal("8000.00"), finalDest.getBalance());
    }

    @Test
    @DisplayName("Should record transfer with description")
    void testTransfer_RecordsDescription() throws Exception {
        // Arrange
        String transferDescription = "Payment for services";
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            transferDescription
        );

        // Act
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());

        // Assert
        List<Transaction> transactions = transactionRepository.findByAccountId(sourceAccount.getId());
        assert !transactions.isEmpty();
        assertEquals(transferDescription, transactions.get(0).getDescription());
    }

    @Test
    @DisplayName("Should handle zero transfer amount")
    void testTransfer_ZeroAmount() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            BigDecimal.ZERO,
            "Zero Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle negative transfer amount")
    void testTransfer_NegativeAmount() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("-1000.00"),
            "Negative Transfer"
        );

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should retrieve transfer transactions for both accounts")
    void testTransfer_TransactionsRetrievable() throws Exception {
        // Arrange
        TransferRequest transferRequest = new TransferRequest(
            sourceAccount.getId(),
            destinationAccount.getId(),
            new BigDecimal("1000.00"),
            "Test Transfer"
        );

        // Act
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());

        // Assert - Retrieve transactions for source account
        mockMvc.perform(get("/api/accounts/" + sourceAccount.getId() + "/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("TRANSFER"))
                .andExpect(jsonPath("$[0].amount").value(1000.00));

        // Assert - Retrieve transactions for destination account
        mockMvc.perform(get("/api/accounts/" + destinationAccount.getId() + "/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("TRANSFER"))
                .andExpect(jsonPath("$[0].amount").value(1000.00));
    }
}

