package org.example.productjpa.controller;

import jakarta.validation.Valid;
import org.example.productjpa.dto.CustomerRequestDto;
import org.example.productjpa.dto.CustomerResponseDto;
import org.example.productjpa.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

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

    // Custom Query Endpoints

    // Get customers without orders
    @GetMapping("/without-orders")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersWithoutOrders() {
        List<CustomerResponseDto> customers = customerService.getCustomersWithoutOrders();
        return ResponseEntity.ok(customers);
    }

    // Find customers by email domain
    @GetMapping("/by-domain/{domain}")
    public ResponseEntity<List<CustomerResponseDto>> findByEmailDomain(@PathVariable String domain) {
        List<CustomerResponseDto> customers = customerService.findByEmailDomain(domain);
        return ResponseEntity.ok(customers);
    }

    // Get top customers by order count
    @GetMapping("/top-customers")
    public ResponseEntity<List<CustomerResponseDto>> getTopCustomersByOrderCount() {
        List<CustomerResponseDto> customers = customerService.getTopCustomersByOrderCount();
        return ResponseEntity.ok(customers);
    }

    // Get customers with minimum number of orders
    @GetMapping("/with-min-orders/{minOrders}")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersWithMinOrders(@PathVariable long minOrders) {
        List<CustomerResponseDto> customers = customerService.getCustomersWithMinOrders(minOrders);
        return ResponseEntity.ok(customers);
    }

    // UPDATE: Update customer address
    @PutMapping("/{id}/update-address")
    public ResponseEntity<String> updateCustomerAddress(
            @PathVariable Long id,
            @RequestParam String address) {
        int updated = customerService.updateCustomerAddress(id, address);
        return ResponseEntity.ok("Customer address updated. Records affected: " + updated);
    }

    // UPDATE: Update customer contact information
    @PutMapping("/{id}/update-contact")
    public ResponseEntity<String> updateCustomerContactInfo(
            @PathVariable Long id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address) {
        int updated = customerService.updateCustomerContactInfo(id, phone, address);
        return ResponseEntity.ok("Customer contact info updated. Records affected: " + updated);
    }
}
