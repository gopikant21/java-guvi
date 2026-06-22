package org.example.productjpa.services;

import org.example.productjpa.exceptions.ResourceNotFoundException;
import org.example.productjpa.model.Customer;
import org.example.productjpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Create
    public Customer registerCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Customer with this email already exists");
        }
        return customerRepository.save(customer);
    }

    // Read
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.customerNotFound(id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
    }

    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone: " + phone));
    }

    // Update
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);

        if (customerDetails.getName() != null && !customerDetails.getName().isEmpty()) {
            customer.setName(customerDetails.getName());
        }
        if (customerDetails.getEmail() != null && !customerDetails.getEmail().isEmpty()) {
            // Check if email is not already taken by another customer
            if (customerRepository.existsByEmail(customerDetails.getEmail())
                && !customerDetails.getEmail().equals(customer.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            customer.setEmail(customerDetails.getEmail());
        }
        if (customerDetails.getAddress() != null && !customerDetails.getAddress().isEmpty()) {
            customer.setAddress(customerDetails.getAddress());
        }
        if (customerDetails.getPhone() != null && !customerDetails.getPhone().isEmpty()) {
            customer.setPhone(customerDetails.getPhone());
        }

        return customerRepository.save(customer);
    }

    // Delete
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}

