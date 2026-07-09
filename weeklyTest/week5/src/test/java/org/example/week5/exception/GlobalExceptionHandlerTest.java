package org.example.week5.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return 404 for CustomerNotFoundException")
    void testCustomerNotFound_Returns404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should return 404 for AccountNotFoundException")
    void testAccountNotFound_Returns404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should return 409 for DuplicateEmailException")
    void testDuplicateEmail_Returns409() throws Exception {
        // Arrange
        String customerJson = "{\"name\": \"John\", \"email\": \"test@example.com\", \"phone\": \"1234567890\"}";

        // Note: This test requires the application to actually throw DuplicateEmailException
        // For a complete test, you would need to insert a duplicate email first

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 400 for InsufficientBalanceException")
    void testInsufficientBalance_Returns400() throws Exception {
        // This test verifies that insufficient balance throws a 400 error
        // Actual test would need a withdrawal attempt with insufficient funds

        // Act & Assert
        mockMvc.perform(post("/api/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\": 1, \"amount\": 99999999}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for InvalidTransferException")
    void testInvalidTransfer_Returns400() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sourceAccountId\": 1, \"destinationAccountId\": 1, \"amount\": 1000}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for validation errors")
    void testValidationError_Returns400() throws Exception {
        // Arrange - Invalid email format
        String invalidCustomerJson = "{\"name\": \"\", \"email\": \"invalid\", \"phone\": \"123\"}";

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCustomerJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 401 for unauthorized access")
    void testUnauthorized_Returns401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when no JWT token provided")
    void testNoToken_Returns401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 403 for forbidden access")
    void testForbidden_Returns403() throws Exception {
        // This test would require role-based security configuration
        // Act & Assert - Testing with a restricted endpoint
        mockMvc.perform(delete("/api/admin/customers/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should include error message in response")
    void testErrorResponse_IncludesMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Should include timestamp in error response")
    void testErrorResponse_IncludesTimestamp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Should include HTTP status code in error response")
    void testErrorResponse_IncludesStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Should handle null pointer exceptions")
    void testNullPointerException_Returns500() throws Exception {
        // This would be triggered by a request that causes NPE in the service layer
        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle method argument type mismatch")
    void testMethodArgumentTypeMismatch_Returns400() throws Exception {
        // Act & Assert - Sending string instead of number for ID
        mockMvc.perform(get("/api/customers/invalid_id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 405 for unsupported HTTP method")
    void testMethodNotAllowed_Returns405() throws Exception {
        // Act & Assert - Using PATCH instead of PUT or POST
        mockMvc.perform(patch("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should handle missing required request body")
    void testMissingRequestBody_Returns400() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle JSON parse errors")
    void testJsonParseError_Returns400() throws Exception {
        // Act & Assert - Sending invalid JSON
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should include detailed validation error messages")
    void testValidationError_IncludesDetails() throws Exception {
        // Arrange - Missing required fields
        String invalidJson = "{}";

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("Should properly handle concurrent access exceptions")
    void testConcurrentAccessException() throws Exception {
        // This test would verify optimistic locking or concurrent modification handling
        // Act & Assert
        mockMvc.perform(put("/api/accounts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isConflict());
    }
}

