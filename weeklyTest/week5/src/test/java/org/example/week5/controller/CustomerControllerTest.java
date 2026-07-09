package org.example.week5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.CustomerDTO;
import org.example.week5.exception.CustomerNotFoundException;
import org.example.week5.exception.DuplicateEmailException;
import org.example.week5.service.CustomerService;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Customer Controller Tests")
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setId(1L);
        testCustomerDTO.setName("John Doe");
        testCustomerDTO.setEmail("john@example.com");
        testCustomerDTO.setPhone("9876543210");
    }

    @Test
    @DisplayName("Should create customer with POST /api/customers")
    void testCreateCustomer_Success() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(testCustomerDTO);

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(customerService, times(1)).createCustomer(any(CustomerDTO.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when duplicate email on creation")
    void testCreateCustomer_DuplicateEmail() throws Exception {
        // Arrange
        when(customerService.createCustomer(any(CustomerDTO.class)))
                .thenThrow(new DuplicateEmailException("Email already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid customer data")
    void testCreateCustomer_InvalidData() throws Exception {
        // Arrange
        CustomerDTO invalidDTO = new CustomerDTO();
        invalidDTO.setName(""); // Empty name
        invalidDTO.setEmail("invalid-email");
        invalidDTO.setPhone("12345");

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).createCustomer(any(CustomerDTO.class));
    }

    @Test
    @DisplayName("Should retrieve all customers with GET /api/customers")
    void testGetAllCustomers() throws Exception {
        // Arrange
        CustomerDTO customer2 = new CustomerDTO();
        customer2.setId(2L);
        customer2.setName("Jane Doe");
        customer2.setEmail("jane@example.com");
        customer2.setPhone("1234567890");

        when(customerService.getAllCustomers())
                .thenReturn(Arrays.asList(testCustomerDTO, customer2));

        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should retrieve customer by id with GET /api/customers/{id}")
    void testGetCustomerById_Success() throws Exception {
        // Arrange
        when(customerService.getCustomerById(1L)).thenReturn(testCustomerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @DisplayName("Should return 404 Not Found when customer not found by id")
    void testGetCustomerById_NotFound() throws Exception {
        // Arrange
        when(customerService.getCustomerById(99L))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        // Act & Assert
        mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update customer with PUT /api/customers/{id}")
    void testUpdateCustomer_Success() throws Exception {
        // Arrange
        CustomerDTO updatedDTO = new CustomerDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Jane Doe");
        updatedDTO.setEmail("jane@example.com");
        updatedDTO.setPhone("1234567890");

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class)))
                .thenReturn(updatedDTO);

        // Act & Assert
        mockMvc.perform(put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should return 404 Not Found when updating non-existent customer")
    void testUpdateCustomer_NotFound() throws Exception {
        // Arrange
        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class)))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        // Act & Assert
        mockMvc.perform(put("/api/customers/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete customer with DELETE /api/customers/{id}")
    void testDeleteCustomer_Success() throws Exception {
        // Arrange
        doNothing().when(customerService).deleteCustomer(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    @DisplayName("Should return 404 Not Found when deleting non-existent customer")
    void testDeleteCustomer_NotFound() throws Exception {
        // Arrange
        doThrow(new CustomerNotFoundException("Customer not found"))
                .when(customerService).deleteCustomer(99L);

        // Act & Assert
        mockMvc.perform(delete("/api/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should validate email format on creation")
    void testCreateCustomer_InvalidEmailFormat() throws Exception {
        // Arrange
        CustomerDTO invalidEmailDTO = new CustomerDTO();
        invalidEmailDTO.setName("Test User");
        invalidEmailDTO.setEmail("invalid-email");
        invalidEmailDTO.setPhone("1234567890");

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should validate phone number on creation")
    void testCreateCustomer_InvalidPhoneNumber() throws Exception {
        // Arrange
        CustomerDTO invalidPhoneDTO = new CustomerDTO();
        invalidPhoneDTO.setName("Test User");
        invalidPhoneDTO.setEmail("test@example.com");
        invalidPhoneDTO.setPhone("12345"); // Invalid: not 10 digits

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPhoneDTO)))
                .andExpect(status().isBadRequest());
    }
}

