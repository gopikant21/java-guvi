package org.example.week5.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.AuthDTO;
import org.example.week5.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Security and Authentication Tests")
class SecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthDTO authDTO;

    @BeforeEach
    void setUp() {
        authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password123");
    }

    @Test
    @DisplayName("Should allow public access to /api/auth/register")
    void testRegisterEndpoint_IsPublic() throws Exception {
        // Arrange
        when(authService.register(any(AuthDTO.class))).thenReturn("User registered successfully");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow public access to /api/auth/login")
    void testLoginEndpoint_IsPublic() throws Exception {
        // Arrange
        when(authService.login(any(AuthDTO.class))).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should register customer with valid email and password")
    void testRegister_Success() throws Exception {
        // Arrange
        when(authService.register(any(AuthDTO.class))).thenReturn("User registered successfully");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @DisplayName("Should return 400 for invalid email format in register")
    void testRegister_InvalidEmail() throws Exception {
        // Arrange
        AuthDTO invalidDTO = new AuthDTO();
        invalidDTO.setEmail("invalid-email");
        invalidDTO.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should login with valid credentials")
    void testLogin_Success() throws Exception {
        // Arrange
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.signature";
        when(authService.login(any(AuthDTO.class))).thenReturn(jwtToken);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(jwtToken));
    }

    @Test
    @DisplayName("Should return 401 for invalid credentials on login")
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        when(authService.login(any(AuthDTO.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should require JWT token for protected endpoints")
    void testProtectedEndpoint_RequiresToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when JWT token is missing")
    void testProtectedEndpoint_MissingToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when JWT token is invalid")
    void testProtectedEndpoint_InvalidToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when JWT token is expired")
    void testProtectedEndpoint_ExpiredToken() throws Exception {
        // Arrange - This assumes a token validation mechanism that checks expiration
        String expiredToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDAwMDB9.signature";

        // Act & Assert
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 401 when Authorization header has wrong format")
    void testProtectedEndpoint_WrongAuthFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers")
                .header("Authorization", "Basic invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow access to /api/customers with valid JWT")
    void testProtectedEndpoint_ValidToken() throws Exception {
        // Arrange
        String validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.signature";

        // Act & Assert - This should return 200 if token is valid (implementation dependent)
        mockMvc.perform(get("/api/customers")
                .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow access to /api/accounts with valid JWT")
    void testAccountEndpoint_ValidToken() throws Exception {
        // Arrange
        String validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.signature";

        // Act & Assert
        mockMvc.perform(get("/api/accounts")
                .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow access to /api/transactions with valid JWT")
    void testTransactionEndpoint_ValidToken() throws Exception {
        // Arrange
        String validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0In0.signature";

        // Act & Assert
        mockMvc.perform(get("/api/transactions")
                .header("Authorization", validToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should protect POST endpoints with JWT authentication")
    void testProtectedPostEndpoint_RequiresToken() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should protect PUT endpoints with JWT authentication")
    void testProtectedPutEndpoint_RequiresToken() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should protect DELETE endpoints with JWT authentication")
    void testProtectedDeleteEndpoint_RequiresToken() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 403 Forbidden for insufficient permissions")
    void testEndpoint_InsufficientPermissions() throws Exception {
        // This test assumes role-based access control implementation
        String userToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiVVNFUiJ9.signature";

        // Act & Assert
        mockMvc.perform(delete("/api/customers/1")
                .header("Authorization", userToken))
                .andExpect(status().isForbidden());
    }
}

