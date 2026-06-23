package org.example.productjpa.controller;

import jakarta.validation.Valid;
import org.example.productjpa.dto.CustomerRequestDto;
import org.example.productjpa.dto.CustomerResponseDto;
import org.example.productjpa.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Register customer
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerResponseDto newCustomer = customerService.registerCustomer(customerRequestDto);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    // Get all customers
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Get customer by email
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
        CustomerResponseDto customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customer);
    }

    // Get customer by phone
    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerResponseDto> getCustomerByPhone(@PathVariable String phone) {
        CustomerResponseDto customer = customerService.getCustomerByPhone(phone);
        return ResponseEntity.ok(customer);
    }

    // Update customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDto customerRequestDto) {
        CustomerResponseDto updatedCustomer = customerService.updateCustomer(id, customerRequestDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Delete customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }
}

