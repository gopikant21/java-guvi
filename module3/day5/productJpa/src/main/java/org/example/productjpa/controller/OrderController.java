package org.example.productjpa.controller;

import org.example.productjpa.model.Order;
import org.example.productjpa.model.OrderItem;
import org.example.productjpa.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create order for customer
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long customerId) {
        Order newOrder = orderService.createOrder(customerId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get orders for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable Long customerId) {
        List<Order> orders = orderService.getCustomerOrders(customerId);
        return ResponseEntity.ok(orders);
    }

    // Get orders for a customer sorted by newest first
    @GetMapping("/customer/{customerId}/newest")
    public ResponseEntity<List<Order>> getCustomerOrdersSortedByNewest(@PathVariable Long customerId) {
        List<Order> orders = orderService.getCustomerOrdersSortedByNewest(customerId);
        return ResponseEntity.ok(orders);
    }

    // Add item to order
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItem> addItemToOrder(
            @PathVariable Long orderId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        OrderItem orderItem = orderService.addItemToOrder(orderId, productId, quantity);
        return new ResponseEntity<>(orderItem, HttpStatus.CREATED);
    }

    // Get items in an order
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        List<OrderItem> items = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(items);
    }

    // Update item quantity in order
    @PutMapping("/items/{orderItemId}")
    public ResponseEntity<OrderItem> updateOrderItemQuantity(
            @PathVariable Long orderItemId,
            @RequestParam int quantity) {
        OrderItem updatedItem = orderService.updateOrderItemQuantity(orderItemId, quantity);
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
    public ResponseEntity<Map<String, Double>> getOrderTotal(@PathVariable Long orderId) {
        Double total = orderService.calculateOrderTotal(orderId);
        Map<String, Double> response = new HashMap<>();
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // Cancel order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled and stock restored");
    }
}

