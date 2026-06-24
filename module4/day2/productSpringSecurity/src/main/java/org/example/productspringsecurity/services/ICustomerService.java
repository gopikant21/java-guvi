package org.example.productspringsecurity.services;

import org.example.productspringsecurity.dto.CustomerRequestDto;
import org.example.productspringsecurity.dto.CustomerResponseDto;

import java.util.List;

public interface ICustomerService {

    // Create
    CustomerResponseDto registerCustomer(CustomerRequestDto customerRequestDto);

    // Read
    CustomerResponseDto getCustomerById(Long id);

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomerByEmail(String email);

    CustomerResponseDto getCustomerByPhone(String phone);

    // Update
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto);

    // Delete
    void deleteCustomer(Long id);

    // Custom Query Methods
    List<CustomerResponseDto> getCustomersWithoutOrders();

    List<CustomerResponseDto> findByEmailDomain(String domain);

    List<CustomerResponseDto> getTopCustomersByOrderCount();

    List<CustomerResponseDto> getCustomersWithMinOrders(long minOrders);

    // UPDATE queries
    int updateCustomerAddress(Long customerId, String newAddress);

    int updateCustomerContactInfo(Long customerId, String phone, String address);
}

