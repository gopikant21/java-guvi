package org.example.week5.service;

import org.example.week5.dto.CustomerDTO;
import org.example.week5.entity.Customer;
import org.example.week5.exception.CustomerNotFoundException;
import org.example.week5.exception.DuplicateEmailException;
import org.example.week5.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPhone("9876543210");
        testCustomer.setPassword("encrypted_password");

        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setName("John Doe");
        testCustomerDTO.setEmail("john@example.com");
        testCustomerDTO.setPhone("9876543210");
    }

    @Test
    @DisplayName("Should create customer successfully with valid data")
    void testCreateCustomer_Success() {
        // Arrange
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        CustomerDTO result = customerService.createCustomer(testCustomerDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(customerRepository).existsByEmail("john@example.com");
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void testCreateCustomer_DuplicateEmail() {
        // Arrange
        when(customerRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> {
            customerService.createCustomer(testCustomerDTO);
        });
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve all customers")
    void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Customer 1");
        customer1.setEmail("customer1@example.com");
        customer1.setPhone("1234567890");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Customer 2");
        customer2.setEmail("customer2@example.com");
        customer2.setPhone("0987654321");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        List<CustomerDTO> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve customer by id successfully")
    void testGetCustomerById_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        CustomerDTO result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(customerRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when customer not found by id")
    void testGetCustomerById_NotFound() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(99L);
        });
        verify(customerRepository).findById(99L);
    }

    @Test
    @DisplayName("Should update customer successfully")
    void testUpdateCustomer_Success() {
        // Arrange
        CustomerDTO updateDTO = new CustomerDTO();
        updateDTO.setName("Jane Doe");
        updateDTO.setEmail("jane@example.com");
        updateDTO.setPhone("1234567890");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Jane Doe");
        updatedCustomer.setEmail("jane@example.com");
        updatedCustomer.setPhone("1234567890");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(customerRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        CustomerDTO result = customerService.updateCustomer(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        verify(customerRepository).findById(1L);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when updating non-existent customer")
    void testUpdateCustomer_NotFound() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(99L, testCustomerDTO);
        });
    }

    @Test
    @DisplayName("Should delete customer successfully")
    void testDeleteCustomer_Success() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository).findById(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw CustomerNotFoundException when deleting non-existent customer")
    void testDeleteCustomer_NotFound() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.deleteCustomer(99L);
        });
        verify(customerRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should validate email format")
    void testValidateEmailFormat() {
        // Arrange
        CustomerDTO invalidEmailDTO = new CustomerDTO();
        invalidEmailDTO.setName("Test User");
        invalidEmailDTO.setEmail("invalid-email");
        invalidEmailDTO.setPhone("1234567890");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            customerService.createCustomer(invalidEmailDTO);
        });
    }

    @Test
    @DisplayName("Should validate phone number format (exactly 10 digits)")
    void testValidatePhoneNumber() {
        // Arrange
        CustomerDTO invalidPhoneDTO = new CustomerDTO();
        invalidPhoneDTO.setName("Test User");
        invalidPhoneDTO.setEmail("test@example.com");
        invalidPhoneDTO.setPhone("123456789"); // Only 9 digits

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            customerService.createCustomer(invalidPhoneDTO);
        });
    }

    @Test
    @DisplayName("Should validate mandatory name field")
    void testValidateMandatoryName() {
        // Arrange
        CustomerDTO noNameDTO = new CustomerDTO();
        noNameDTO.setName("");
        noNameDTO.setEmail("test@example.com");
        noNameDTO.setPhone("1234567890");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            customerService.createCustomer(noNameDTO);
        });
    }
}

