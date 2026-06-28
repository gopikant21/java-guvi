package org.example.productjwt.services;

import org.example.productjwt.dto.CustomerRequestDto;
import org.example.productjwt.dto.CustomerResponseDto;
import org.example.productjwt.exceptions.ResourceNotFoundException;
import org.example.productjwt.mapper.CustomerMapper;
import org.example.productjwt.model.Customer;
import org.example.productjwt.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService implements ICustomerService {

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

    // Custom Query Methods

    // Find customers without orders
    public List<CustomerResponseDto> getCustomersWithoutOrders() {
        return customerRepository.findCustomersWithoutOrders().stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Find customers by email domain
    public List<CustomerResponseDto> findByEmailDomain(String domain) {
        return customerRepository.findByEmailDomain(domain).stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get top customers by order count
    public List<CustomerResponseDto> getTopCustomersByOrderCount() {
        return customerRepository.findTopCustomersByOrderCount().stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Get customers with minimum number of orders
    public List<CustomerResponseDto> getCustomersWithMinOrders(long minOrders) {
        if (minOrders < 1) {
            throw new IllegalArgumentException("Minimum orders must be at least 1");
        }
        return customerRepository.findCustomersWithMinOrders(minOrders).stream()
                .map(customerMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // UPDATE: Update customer address
    public int updateCustomerAddress(Long customerId, String newAddress) {
        if (newAddress == null || newAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        // Verify customer exists
        getCustomerById(customerId);
        return customerRepository.updateCustomerAddress(customerId, newAddress);
    }

    // UPDATE: Update customer contact information (phone and address)
    public int updateCustomerContactInfo(Long customerId, String phone, String address) {
        if ((phone == null || phone.trim().isEmpty()) && (address == null || address.trim().isEmpty())) {
            throw new IllegalArgumentException("At least one contact field must be provided");
        }
        // Verify customer exists
        getCustomerById(customerId);
        return customerRepository.updateCustomerContactInfo(customerId, phone, address);
    }
}
