package org.example.emidefaulter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.emidefaulter.entity.Customer;
import org.example.emidefaulter.entity.CustomerRole;
import org.example.emidefaulter.entity.Loan;
import org.example.emidefaulter.exception.GlobalExceptionHandler;
import org.example.emidefaulter.security.JwtUtil;
import org.example.emidefaulter.service.LoanRecoveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoanRecoveryControllerMockMvcTest {

    private MockMvc mockMvc;
    private LoanRecoveryService loanRecoveryService;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        loanRecoveryService = mock(LoanRecoveryService.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);

        LoanRecoveryController controller = new LoanRecoveryController(
                loanRecoveryService,
                authenticationManager,
                jwtUtil
        );

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        MappingJackson2HttpMessageConverter jacksonConverter =
                new MappingJackson2HttpMessageConverter(new ObjectMapper().findAndRegisterModules());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(jacksonConverter)
                .build();
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() throws Exception {
        User userDetails = new User("admin@bank.com", "encoded", List.of());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(userDetails)).thenReturn("dummy-jwt-token");
        when(jwtUtil.getExpirySeconds()).thenReturn(3600L);

        String requestBody = """
                {
                  "email": "admin@bank.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummy-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresInSeconds").value(3600));

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }

    @Test
    void loginReturnsUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String requestBody = """
                {
                  "email": "customer@example.com",
                  "password": "wrong-password"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"))
                .andExpect(jsonPath("$.path").value("/login"));
    }

    @Test
    void loginReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        String requestBody = """
                {
                  "email": "not-an-email",
                  "password": ""
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("Email must be valid")));
    }

    @Test
    void registerCustomerReturnsCreatedWhenPayloadIsValid() throws Exception {
        Customer customer = Customer.builder()
                .customerId(1L)
                .customerName("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("9876543210")
                .password("encoded-password")
                .city("Mumbai")
                .creditScore(750)
                .role(CustomerRole.CUSTOMER)
                .build();

        when(loanRecoveryService.registerCustomer(any(Customer.class))).thenReturn(customer);

        String requestBody = """
                {
                  "customerName": "John Doe",
                  "email": "john.doe@example.com",
                  "phoneNumber": "9876543210",
                  "password": "SecurePass123!",
                  "city": "Mumbai",
                  "creditScore": 750,
                  "role": "CUSTOMER"
                }
                """;

        mockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        verify(loanRecoveryService, times(1)).registerCustomer(any(Customer.class));
    }

    @Test
    void registerCustomerReturnsBadRequestWhenPayloadIsInvalid() throws Exception {
        String requestBody = """
                {
                  "customerName": "",
                  "email": "invalid-email",
                  "phoneNumber": "123",
                  "password": "short",
                  "city": "",
                  "creditScore": 950,
                  "role": null
                }
                """;

        mockMvc.perform(post("/customers")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getAllLoansReturnsPagePayload() throws Exception {
        Page<Loan> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(loanRecoveryService.getLoans(any())).thenReturn(page);

        mockMvc.perform(get("/loans")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "emiAmount")
                        .param("direction", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));

        verify(loanRecoveryService, times(1)).getLoans(any());
    }
}


