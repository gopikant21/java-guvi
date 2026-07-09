package org.example.week5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.TransactionDTO;
import org.example.week5.exception.AccountNotFoundException;
import org.example.week5.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Transaction Controller Tests")
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionDTO testTransactionDTO;

    @BeforeEach
    void setUp() {
        testTransactionDTO = new TransactionDTO();
        testTransactionDTO.setId(1L);
        testTransactionDTO.setTransactionType("DEPOSIT");
        testTransactionDTO.setAmount(new BigDecimal("1000.00"));
        testTransactionDTO.setTransactionDate(LocalDateTime.now());
        testTransactionDTO.setDescription("Salary deposit");
        testTransactionDTO.setAccountId(1L);
    }

    @Test
    @DisplayName("Should retrieve all transactions with GET /api/transactions")
    void testGetAllTransactions() throws Exception {
        // Arrange
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setId(2L);
        transaction2.setTransactionType("WITHDRAWAL");
        transaction2.setAmount(new BigDecimal("500.00"));
        transaction2.setTransactionDate(LocalDateTime.now());
        transaction2.setAccountId(1L);

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(testTransactionDTO, transaction2));

        // Act & Assert
        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].transactionType").value("WITHDRAWAL"));
    }

    @Test
    @DisplayName("Should retrieve transaction by id with GET /api/transactions/{id}")
    void testGetTransactionById_Success() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(anyLong())).thenReturn(testTransactionDTO);

        // Act & Assert
        String responseBody = mockMvc.perform(get("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(1000.00))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TransactionDTO parsed = objectMapper.readValue(responseBody, TransactionDTO.class);
        assertEquals("DEPOSIT", parsed.getTransactionType());
        verify(transactionService, times(1)).getTransactionById(anyLong());
    }

    @Test
    @DisplayName("Should return 404 when transaction not found by id")
    void testGetTransactionById_NotFound() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(99L))
                .thenThrow(new RuntimeException("Transaction not found"));

        // Act & Assert
        mockMvc.perform(get("/api/transactions/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should retrieve all transactions for account with GET /api/accounts/{id}/transactions")
    void testGetTransactionsByAccountId_Success() throws Exception {
        // Arrange
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setId(2L);
        transaction2.setTransactionType("WITHDRAWAL");
        transaction2.setAmount(new BigDecimal("500.00"));
        transaction2.setAccountId(1L);

        when(transactionService.getTransactionsByAccountId(1L))
                .thenReturn(Arrays.asList(testTransactionDTO, transaction2));

        // Act & Assert
        mockMvc.perform(get("/api/accounts/1/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].transactionType").value("WITHDRAWAL"));
    }

    @Test
    @DisplayName("Should return empty list when account has no transactions")
    void testGetTransactionsByAccountId_EmptyList() throws Exception {
        // Arrange
        when(transactionService.getTransactionsByAccountId(1L))
                .thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/accounts/1/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return 404 when account not found for transactions")
    void testGetTransactionsByAccountId_AccountNotFound() throws Exception {
        // Arrange
        when(transactionService.getTransactionsByAccountId(99L))
                .thenThrow(new AccountNotFoundException("Account not found"));

        // Act & Assert
        mockMvc.perform(get("/api/accounts/99/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should retrieve DEPOSIT type transactions")
    void testGetTransactions_DepositType() throws Exception {
        // Arrange
        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(testTransactionDTO));

        // Act & Assert
        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"));
    }

    @Test
    @DisplayName("Should retrieve WITHDRAWAL type transactions")
    void testGetTransactions_WithdrawalType() throws Exception {
        // Arrange
        TransactionDTO withdrawalDTO = new TransactionDTO();
        withdrawalDTO.setId(2L);
        withdrawalDTO.setTransactionType("WITHDRAWAL");
        withdrawalDTO.setAmount(new BigDecimal("500.00"));

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(withdrawalDTO));

        // Act & Assert
        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("WITHDRAWAL"));
    }

    @Test
    @DisplayName("Should retrieve TRANSFER type transactions")
    void testGetTransactions_TransferType() throws Exception {
        // Arrange
        TransactionDTO transferDTO = new TransactionDTO();
        transferDTO.setId(3L);
        transferDTO.setTransactionType("TRANSFER");
        transferDTO.setAmount(new BigDecimal("2000.00"));

        when(transactionService.getAllTransactions())
                .thenReturn(Arrays.asList(transferDTO));

        // Act & Assert
        mockMvc.perform(get("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("TRANSFER"));
    }

    @Test
    @DisplayName("Should include transaction description in response")
    void testGetTransaction_WithDescription() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(1L)).thenReturn(testTransactionDTO);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Salary deposit"));
    }

    @Test
    @DisplayName("Should include transaction date in response")
    void testGetTransaction_WithDate() throws Exception {
        // Arrange
        when(transactionService.getTransactionById(1L)).thenReturn(testTransactionDTO);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionDate").exists());
    }
}

