package org.example.productjpa.controller;

import jakarta.validation.Valid;
import org.example.productjpa.dto.OrderItemRequestDto;
import org.example.productjpa.dto.OrderItemResponseDto;
import org.example.productjpa.dto.OrderResponseDto;
import org.example.productjpa.dto.OrderTotalResponseDto;
import org.example.productjpa.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create order for customer
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long customerId) {
        OrderResponseDto newOrder = orderService.createOrder(customerId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get orders for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrders(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    // Get orders for a customer sorted by newest first
    @GetMapping("/customer/{customerId}/newest")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrdersSortedByNewest(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderService.getCustomerOrdersSortedByNewest(customerId);
        return ResponseEntity.ok(orders);
    }

    // Add item to order
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItem = orderService.addItemToOrder(orderId, orderItemRequestDto);
        return new ResponseEntity<>(orderItem, HttpStatus.CREATED);
    }

    // Get items in an order
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItemResponseDto> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    // Update item quantity in order
    @PutMapping("/items/{orderItemId}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemQuantity(
            @PathVariable Long orderItemId,
            @RequestParam int quantity) {
        OrderItemResponseDto updatedItem = orderService.updateOrderItemQuantity(orderItemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    // Remove item from order
    @DeleteMapping("/items/{orderItemId}")
    public ResponseEntity<String> removeItemFromOrder(@PathVariable Long orderItemId) {
        orderService.removeItemFromOrder(orderItemId);
        return ResponseEntity.ok("Item removed from order");
    }

    // Get order total
    @GetMapping("/{orderId}/total")
    public ResponseEntity<OrderTotalResponseDto> getOrderTotal(@PathVariable Long orderId) {
        OrderTotalResponseDto response = orderService.getOrderTotal(orderId);
        return ResponseEntity.ok(response);
    }

    // Cancel order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled and stock restored");
    }
}

