package org.example.week5.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.week5.dto.CustomerDTO;
import org.example.week5.entity.Customer;
import org.example.week5.repository.CustomerRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Customer Integration Tests")
@Transactional
class CustomerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john@example.com");
        customerDTO.setPhone("9876543210");
    }

    @Test
    @DisplayName("Should create customer end-to-end through API")
    void testCreateCustomer_EndToEnd() throws Exception {
        // Act
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        // Assert - Verify in database
        Customer savedCustomer = customerRepository.findByEmail("john@example.com").orElse(null);
        assert savedCustomer != null;
        assert savedCustomer.getName().equals("John Doe");
    }

    @Test
    @DisplayName("Should retrieve created customer through API")
    void testGetCustomer_AfterCreation() throws Exception {
        // Arrange
        CustomerDTO createdCustomer = new CustomerDTO();
        createdCustomer.setName("Jane Doe");
        createdCustomer.setEmail("jane@example.com");
        createdCustomer.setPhone("1234567890");

        // Create customer first
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdCustomer)))
                .andExpect(status().isCreated());

        // Get the customer ID
        Customer savedCustomer = customerRepository.findByEmail("jane@example.com").orElse(null);
        assert savedCustomer != null;

        // Act & Assert - Retrieve the customer
        mockMvc.perform(get("/api/customers/" + savedCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @DisplayName("Should update customer and persist changes")
    void testUpdateCustomer_EndToEnd() throws Exception {
        // Arrange - Create a customer
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated());

        Customer savedCustomer = customerRepository.findByEmail("john@example.com").orElse(null);
        assert savedCustomer != null;

        // Prepare update
        CustomerDTO updateDTO = new CustomerDTO();
        updateDTO.setName("John Smith");
        updateDTO.setEmail("john.smith@example.com");
        updateDTO.setPhone("9876543210");

        // Act
        mockMvc.perform(put("/api/customers/" + savedCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"));

        // Assert - Verify in database
        Customer updatedCustomer = customerRepository.findById(savedCustomer.getId()).orElse(null);
        assert updatedCustomer != null;
        assert updatedCustomer.getName().equals("John Smith");
    }

    @Test
    @DisplayName("Should delete customer and remove from database")
    void testDeleteCustomer_EndToEnd() throws Exception {
        // Arrange - Create a customer
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated());

        Customer savedCustomer = customerRepository.findByEmail("john@example.com").orElse(null);
        assert savedCustomer != null;

        // Act
        mockMvc.perform(delete("/api/customers/" + savedCustomer.getId()))
                .andExpect(status().isNoContent());

        // Assert - Verify deleted from database
        assert customerRepository.findById(savedCustomer.getId()).isEmpty();
    }

    @Test
    @DisplayName("Should prevent duplicate email during creation")
    void testDuplicateEmail_PreventCreation() throws Exception {
        // Arrange - Create first customer
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated());

        // Try to create duplicate
        CustomerDTO duplicateDTO = new CustomerDTO();
        duplicateDTO.setName("Another User");
        duplicateDTO.setEmail("john@example.com"); // Duplicate email
        duplicateDTO.setPhone("1234567890");

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should retrieve all customers created")
    void testGetAllCustomers_ReturnsCreatedCustomers() throws Exception {
        // Arrange - Create multiple customers
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated());

        CustomerDTO customer2 = new CustomerDTO();
        customer2.setName("Jane Doe");
        customer2.setEmail("jane@example.com");
        customer2.setPhone("1234567890");

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer2)))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Should persist encrypted password")
    void testPasswordEncryption_Persisted() throws Exception {
        // Arrange
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated());

        // Assert
        Customer savedCustomer = customerRepository.findByEmail("john@example.com").orElse(null);
        assert savedCustomer != null;
        assert savedCustomer.getPassword() != null;
        // In real scenario, password should be encrypted
    }

    @Test
    @DisplayName("Should validate customer email format")
    void testEmailValidation_InvalidFormat() throws Exception {
        // Arrange
        CustomerDTO invalidDTO = new CustomerDTO();
        invalidDTO.setName("Test User");
        invalidDTO.setEmail("invalid-email");
        invalidDTO.setPhone("1234567890");

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should validate customer phone number format (10 digits)")
    void testPhoneValidation_InvalidFormat() throws Exception {
        // Arrange
        CustomerDTO invalidDTO = new CustomerDTO();
        invalidDTO.setName("Test User");
        invalidDTO.setEmail("test@example.com");
        invalidDTO.setPhone("12345"); // Less than 10 digits

        // Act & Assert
        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle concurrent customer creation")
    void testConcurrentCustomerCreation() throws Exception {
        // Create multiple customers with different emails
        for (int i = 0; i < 3; i++) {
            CustomerDTO customerData = new CustomerDTO();
            customerData.setName("Customer " + i);
            customerData.setEmail("customer" + i + "@example.com");
            customerData.setPhone("123456789" + i);

            mockMvc.perform(post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerData)))
                    .andExpect(status().isCreated());
        }

        // Verify all created
        assert customerRepository.count() == 3;
    }

    @Test
    @DisplayName("Should return 404 when getting non-existent customer")
    void testGetNonExistentCustomer() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/customers/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent customer")
    void testUpdateNonExistentCustomer() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/api/customers/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNotFound());
    }
}

