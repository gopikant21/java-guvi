package org.example.productjwt.controller;

import jakarta.validation.Valid;
import org.example.productjwt.dto.OrderItemRequestDto;
import org.example.productjwt.dto.OrderItemResponseDto;
import org.example.productjwt.dto.OrderResponseDto;
import org.example.productjwt.dto.OrderTotalResponseDto;
import org.example.productjwt.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    // Create order for customer
    @PostMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long customerId) {
        OrderResponseDto newOrder = orderService.createOrder(customerId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // Get all orders
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get orders for a customer
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrders(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    // Get orders for a customer sorted by newest first
    @GetMapping("/customer/{customerId}/newest")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrdersSortedByNewest(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderService.getCustomerOrdersSortedByNewest(customerId);
        return ResponseEntity.ok(orders);
    }

    // Add item to order
    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItem = orderService.addItemToOrder(orderId, orderItemRequestDto);
        return new ResponseEntity<>(orderItem, HttpStatus.CREATED);
    }

    // Get items in an order
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItemResponseDto> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    // Update item quantity in order
    @PutMapping("/items/{orderItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemQuantity(
            @PathVariable Long orderItemId,
            @RequestParam int quantity) {
        OrderItemResponseDto updatedItem = orderService.updateOrderItemQuantity(orderItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    // Remove item from order
    @DeleteMapping("/items/{orderItemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> removeItemFromOrder(@PathVariable Long orderItemId) {
        orderService.removeItemFromOrder(orderItemId);
        return ResponseEntity.ok("Item removed from order");
    }

    // Get order total
    @GetMapping("/{orderId}/total")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderTotalResponseDto> getOrderTotal(@PathVariable Long orderId) {
        OrderTotalResponseDto response = orderService.getOrderTotal(orderId);
        return ResponseEntity.ok(response);
    }

    // Cancel order
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled and stock restored");
    }

    // Custom Query Endpoints

    // Get total spent by customer
    @GetMapping("/customer/{customerId}/total-spent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getTotalSpentByCustomer(@PathVariable Long customerId) {
        Double totalSpent = orderService.getTotalSpentByCustomer(customerId);
        return ResponseEntity.ok(totalSpent);
    }

    // Get high-value orders above threshold
    @GetMapping("/high-value/{threshold}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getHighValueOrders(@PathVariable Double threshold) {
        List<OrderResponseDto> orders = orderService.getHighValueOrders(threshold);
        return ResponseEntity.ok(orders);
    }

    // Count orders for customer
    @GetMapping("/customer/{customerId}/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countOrdersByCustomer(@PathVariable Long customerId) {
        long count = orderService.countOrdersByCustomer(customerId);
        return ResponseEntity.ok(count);
    }

    // Get average order item value for customer
    @GetMapping("/customer/{customerId}/avg-item-value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Double> getAverageOrderItemValueByCustomer(@PathVariable Long customerId) {
        Double avgValue = orderService.getAverageOrderItemValueByCustomer(customerId);
        return ResponseEntity.ok(avgValue);
    }

    // Get empty orders (orders with no items)
    @GetMapping("/empty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getEmptyOrders() {
        List<OrderResponseDto> orders = orderService.getEmptyOrders();
        return ResponseEntity.ok(orders);
    }
}
