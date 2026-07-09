package org.example.week5.repository;

import org.example.week5.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPhone("9876543210");
        testCustomer.setPassword("encrypted_password");
    }

    @Test
    @DisplayName("Should save customer successfully")
    void testSaveCustomer() {
        // Arrange & Act
        Customer savedCustomer = customerRepository.save(testCustomer);

        // Assert
        assertNotNull(savedCustomer.getId());
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("john@example.com", savedCustomer.getEmail());
    }

    @Test
    @DisplayName("Should find customer by email")
    void testFindByEmail() {
        // Arrange
        customerRepository.save(testCustomer);

        // Act
        Optional<Customer> foundCustomer = customerRepository.findByEmail("john@example.com");

        // Assert
        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByEmail_NotFound() {
        // Act
        Optional<Customer> foundCustomer = customerRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    @DisplayName("Should check if email exists")
    void testExistsByEmail() {
        // Arrange
        customerRepository.save(testCustomer);

        // Act
        boolean exists = customerRepository.existsByEmail("john@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void testExistsByEmail_NotExists() {
        // Act
        boolean exists = customerRepository.existsByEmail("notexist@example.com");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should retrieve customer by id")
    void testFindById() {
        // Arrange
        Customer savedCustomer = customerRepository.save(testCustomer);

        // Act
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());

        // Assert
        assertTrue(foundCustomer.isPresent());
        assertEquals("John Doe", foundCustomer.get().getName());
    }

    @Test
    @DisplayName("Should update customer")
    void testUpdateCustomer() {
        // Arrange
        Customer savedCustomer = customerRepository.save(testCustomer);
        savedCustomer.setName("Jane Doe");

        // Act
        Customer updatedCustomer = customerRepository.save(savedCustomer);

        // Assert
        assertEquals("Jane Doe", updatedCustomer.getName());
    }

    @Test
    @DisplayName("Should delete customer")
    void testDeleteCustomer() {
        // Arrange
        Customer savedCustomer = customerRepository.save(testCustomer);
        Long customerId = savedCustomer.getId();

        // Act
        customerRepository.deleteById(customerId);

        // Assert
        assertFalse(customerRepository.findById(customerId).isPresent());
    }

    @Test
    @DisplayName("Should enforce email uniqueness constraint")
    void testEmailUniquenessConstraint() {
        // Arrange
        customerRepository.save(testCustomer);

        Customer duplicateCustomer = new Customer();
        duplicateCustomer.setName("Jane Doe");
        duplicateCustomer.setEmail("john@example.com");
        duplicateCustomer.setPhone("1234567890");
        duplicateCustomer.setPassword("encrypted");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            customerRepository.save(duplicateCustomer);
            customerRepository.flush();
        });
    }

    @Test
    @DisplayName("Should count total customers")
    void testCountCustomers() {
        // Arrange
        customerRepository.save(testCustomer);

        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");
        customer2.setEmail("jane@example.com");
        customer2.setPhone("1234567890");
        customerRepository.save(customer2);

        // Act
        long count = customerRepository.count();

        // Assert
        assertEquals(2, count);
    }
}

