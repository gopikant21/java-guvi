package org.example.productjpa.services;

import org.example.productjpa.dto.CustomerRequestDto;
import org.example.productjpa.dto.CustomerResponseDto;
import org.example.productjpa.exceptions.ResourceNotFoundException;
import org.example.productjpa.mapper.CustomerMapper;
import org.example.productjpa.model.Customer;
import org.example.productjpa.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    // Create
    public CustomerResponseDto registerCustomer(CustomerRequestDto customerRequestDto) {
        // Check if email already exists
        boolean emailExists = customerRepository.findAll().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(customerRequestDto.getEmail()));

        if (emailExists) {
            throw new IllegalArgumentException("Customer with this email already exists");
        }
        Customer customer = customerMapper.toEntity(customerRequestDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    // Read
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.customerNotFound(id));
        return customerMapper.toResponseDto(customer);
    }

    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public CustomerResponseDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return customerMapper.toResponseDto(customer);
    }

    public CustomerResponseDto getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findAll().stream()
                .filter(c -> c.getPhone().equalsIgnoreCase(phone))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone: " + phone));
        return customerMapper.toResponseDto(customer);
    }

    // Update
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.customerNotFound(id));

        if (customerRequestDto.getEmail() != null && !customerRequestDto.getEmail().isEmpty()) {
            // Check if email is not already taken by another customer
            boolean emailTaken = customerRepository.findAll().stream()
                    .anyMatch(c -> c.getEmail().equalsIgnoreCase(customerRequestDto.getEmail())
                            && !c.getId().equals(customer.getId()));

            if (emailTaken) {
                throw new IllegalArgumentException("Email already in use");
            }
        }

        customerMapper.updateEntityFromDto(customerRequestDto, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(updatedCustomer);
    }

    // Delete
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.customerNotFound(id));
        customerRepository.delete(customer);
    }

    // Internal method to get entity (used by OrderService)
    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.customerNotFound(id));
    }
}

