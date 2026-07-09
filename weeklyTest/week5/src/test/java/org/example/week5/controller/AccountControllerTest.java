package org.example.week5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.AccountDTO;
import org.example.week5.dto.TransferRequest;
import org.example.week5.exception.AccountNotFoundException;
import org.example.week5.exception.CustomerNotFoundException;
import org.example.week5.exception.InsufficientBalanceException;
import org.example.week5.exception.InvalidTransferException;
import org.example.week5.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Account Controller Tests")
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountDTO testAccountDTO;

    @BeforeEach
    void setUp() {
        testAccountDTO = new AccountDTO();
        testAccountDTO.setId(1L);
        testAccountDTO.setAccountNumber("ACC123456");
        testAccountDTO.setAccountType("SAVINGS");
        testAccountDTO.setBalance(new BigDecimal("10000.00"));
        testAccountDTO.setCustomerId(1L);
    }

    @Test
    @DisplayName("Should create account with POST /api/accounts")
    void testCreateAccount_Success() throws Exception {
        // Arrange
        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(testAccountDTO);

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAccountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));

        verify(accountService, times(1)).createAccount(any(AccountDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when customer not found during account creation")
    void testCreateAccount_CustomerNotFound() throws Exception {
        // Arrange
        when(accountService.createAccount(any(AccountDTO.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAccountDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid account data")
    void testCreateAccount_InvalidData() throws Exception {
        // Arrange
        AccountDTO invalidDTO = new AccountDTO();
        invalidDTO.setAccountNumber("");
        invalidDTO.setAccountType("INVALID");
        invalidDTO.setBalance(new BigDecimal("-1000.00"));

        // Act & Assert
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should retrieve all accounts with GET /api/accounts")
    void testGetAllAccounts() throws Exception {
        // Arrange
        AccountDTO account2 = new AccountDTO();
        account2.setId(2L);
        account2.setAccountNumber("ACC789012");
        account2.setAccountType("CURRENT");
        account2.setBalance(new BigDecimal("5000.00"));

        when(accountService.getAllAccounts())
                .thenReturn(Arrays.asList(testAccountDTO, account2));

        // Act & Assert
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$[1].accountNumber").value("ACC789012"));

        verify(accountService, times(1)).getAllAccounts();
    }

    @Test
    @WithMockUser
    @DisplayName("Should retrieve account by id with GET /api/accounts/{id}")
    void testGetAccountById_Success() throws Exception {
        // Arrange
        when(accountService.getAccountById(1L)).thenReturn(testAccountDTO);

        // Act & Assert
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 404 Not Found when account not found by id")
    void testGetAccountById_NotFound() throws Exception {
        // Arrange
        when(accountService.getAccountById(99L))
                .thenThrow(new AccountNotFoundException("Account not found"));

        // Act & Assert
        mockMvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Should update account with PUT /api/accounts/{id}")
    void testUpdateAccount_Success() throws Exception {
        // Arrange
        AccountDTO updatedDTO = new AccountDTO();
        updatedDTO.setId(1L);
        updatedDTO.setAccountType("CURRENT");
        updatedDTO.setBalance(new BigDecimal("15000.00"));

        when(accountService.updateAccount(anyLong(), any(AccountDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType").value("CURRENT"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should delete account with DELETE /api/accounts/{id}")
    void testDeleteAccount_Success() throws Exception {
        // Arrange
        doNothing().when(accountService).deleteAccount(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/accounts/1"))
                .andExpect(status().isNoContent());

        verify(accountService).deleteAccount(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("Should deposit amount with POST /api/accounts/deposit")
    void testDeposit_Success() throws Exception {
        // Arrange
        doNothing().when(accountService).deposit(anyLong(), any(BigDecimal.class), anyString());

        String depositRequest = "{\"accountId\": 1, \"amount\": 5000.00, \"description\": \"Deposit\"}";

        // Act & Assert
        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositRequest))
                .andExpect(status().isOk());

        verify(accountService, times(1)).deposit(anyLong(), any(BigDecimal.class), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 Bad Request for zero or negative deposit")
    void testDeposit_InvalidAmount() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Amount must be greater than zero"))
                .when(accountService).deposit(anyLong(), any(BigDecimal.class), anyString());

        String depositRequest = "{\"accountId\": 1, \"amount\": 0, \"description\": \"Deposit\"}";

        // Act & Assert
        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 404 when depositing to non-existent account")
    void testDeposit_AccountNotFound() throws Exception {
        // Arrange
        doThrow(new AccountNotFoundException("Account not found"))
                .when(accountService).deposit(anyLong(), any(BigDecimal.class), anyString());

        String depositRequest = "{\"accountId\": 99, \"amount\": 1000.00, \"description\": \"Deposit\"}";

        // Act & Assert
        mockMvc.perform(post("/api/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(depositRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Should withdraw amount with POST /api/accounts/withdraw")
    void testWithdraw_Success() throws Exception {
        // Arrange
        doNothing().when(accountService).withdraw(anyLong(), any(BigDecimal.class), anyString());

        String withdrawRequest = "{\"accountId\": 1, \"amount\": 2000.00, \"description\": \"Withdrawal\"}";

        // Act & Assert
        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withdrawRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 Bad Request for insufficient balance on withdrawal")
    void testWithdraw_InsufficientBalance() throws Exception {
        // Arrange
        doThrow(new InsufficientBalanceException("Insufficient balance"))
                .when(accountService).withdraw(anyLong(), any(BigDecimal.class), anyString());

        String withdrawRequest = "{\"accountId\": 1, \"amount\": 50000.00, \"description\": \"Withdrawal\"}";

        // Act & Assert
        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(withdrawRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should transfer amount with POST /api/accounts/transfer")
    void testTransfer_Success() throws Exception {
        // Arrange
        doNothing().when(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyString());

        TransferRequest transferRequest = new TransferRequest(1L, 2L, new BigDecimal("2000.00"), "Transfer");

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 400 when transferring to same account")
    void testTransfer_SameAccount() throws Exception {
        // Arrange
        doThrow(new InvalidTransferException("Cannot transfer to same account"))
                .when(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyString());

        TransferRequest transferRequest = new TransferRequest(1L, 1L, new BigDecimal("1000.00"), "Transfer");

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 404 when source account not found during transfer")
    void testTransfer_SourceAccountNotFound() throws Exception {
        // Arrange
        doThrow(new AccountNotFoundException("Source account not found"))
                .when(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyString());

        TransferRequest transferRequest = new TransferRequest(99L, 2L, new BigDecimal("1000.00"), "Transfer");

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return 404 when destination account not found during transfer")
    void testTransfer_DestinationAccountNotFound() throws Exception {
        // Arrange
        doThrow(new AccountNotFoundException("Destination account not found"))
                .when(accountService).transfer(anyLong(), anyLong(), any(BigDecimal.class), anyString());

        TransferRequest transferRequest = new TransferRequest(1L, 99L, new BigDecimal("1000.00"), "Transfer");

        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isNotFound());
    }
}

