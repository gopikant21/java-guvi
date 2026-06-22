package org.example.productjpa.services;

import org.example.productjpa.exceptions.InvalidOrderException;
import org.example.productjpa.exceptions.ResourceNotFoundException;
import org.example.productjpa.model.Customer;
import org.example.productjpa.model.Order;
import org.example.productjpa.model.OrderItem;
import org.example.productjpa.model.Product;
import org.example.productjpa.repository.OrderRepository;
import org.example.productjpa.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    // Create order
    public Order createOrder(Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        Order order = new Order();
        order.setCustomer(customer);
        return orderRepository.save(order);
    }

    // Read
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getCustomerOrders(Long customerId) {
        // Verify customer exists
        customerService.getCustomerById(customerId);
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getCustomerOrdersSortedByNewest(Long customerId) {
        customerService.getCustomerById(customerId);
        return orderRepository.findOrdersByCustomerIdOrderByNewest(customerId);
    }

    // Add item to order
    public OrderItem addItemToOrder(Long orderId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Order order = getOrderById(orderId);
        Product product = productService.getProductById(productId);

        // Check stock
        if (product.getStocks() < quantity) {
            throw InvalidOrderException.insufficientStock(product.getName(), quantity, product.getStocks());
        }

        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);

        // Reduce product stock
        productService.reduceStock(productId, quantity);

        return orderItemRepository.save(orderItem);
    }

    // Remove item from order (and restore stock)
    public void removeItemFromOrder(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> ResourceNotFoundException.orderItemNotFound(orderItemId));

        // Restore stock
        productService.increaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());

        orderItemRepository.delete(orderItem);
    }

    // Update item quantity
    public OrderItem updateOrderItemQuantity(Long orderItemId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> ResourceNotFoundException.orderItemNotFound(orderItemId));

        Product product = orderItem.getProduct();
        int oldQuantity = orderItem.getQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        // Check if we have enough stock
        if (quantityDifference > 0) {
            if (product.getStocks() < quantityDifference) {
                throw InvalidOrderException.insufficientStock(product.getName(), quantityDifference, product.getStocks());
            }
            productService.reduceStock(product.getId(), quantityDifference);
        } else if (quantityDifference < 0) {
            productService.increaseStock(product.getId(), -quantityDifference);
        }

        orderItem.setQuantity(newQuantity);
        return orderItemRepository.save(orderItem);
    }

    // Get order items
    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = getOrderById(orderId);
        return orderItemRepository.findByOrderId(orderId);
    }

    // Calculate order total
    public Double calculateOrderTotal(Long orderId) {
        List<OrderItem> items = getOrderItems(orderId);
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // Delete order
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        // Restore all stock for items in this order
        for (OrderItem item : order.getOrderItems()) {
            productService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }

        orderRepository.delete(order);
    }
}



