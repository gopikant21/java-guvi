package org.example.productspringsecurity.services;

import org.example.productspringsecurity.dto.OrderItemResponseDto;
import org.example.productspringsecurity.dto.OrderItemRequestDto;
import org.example.productspringsecurity.dto.OrderResponseDto;
import org.example.productspringsecurity.dto.OrderTotalResponseDto;
import org.example.productspringsecurity.exceptions.InvalidOrderException;
import org.example.productspringsecurity.exceptions.ResourceNotFoundException;
import org.example.productspringsecurity.mapper.OrderItemMapper;
import org.example.productspringsecurity.mapper.OrderMapper;
import org.example.productspringsecurity.model.Customer;
import org.example.productspringsecurity.model.Order;
import org.example.productspringsecurity.model.OrderItem;
import org.example.productspringsecurity.model.Product;
import org.example.productspringsecurity.repository.OrderRepository;
import org.example.productspringsecurity.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    // Create order
    public OrderResponseDto createOrder(Long customerId) {
        Customer customer = customerService.getCustomerEntityById(customerId);
        Order order = new Order();
        order.setCustomer(customer);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }

    // Read
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));
        return orderMapper.toResponseDto(order);
    }

    public List<OrderResponseDto> getAllOrders() {
        return orderMapper.toResponseDtoList(orderRepository.findAll());
    }

    public List<OrderResponseDto> getCustomerOrders(Long customerId) {
        // Verify customer exists
        customerService.getCustomerEntityById(customerId);
        return orderRepository.findAll().stream()
                .filter(o -> o.getCustomer().getId().equals(customerId))
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDto> getCustomerOrdersSortedByNewest(Long customerId) {
        customerService.getCustomerEntityById(customerId);
        return orderRepository.findAll().stream()
                .filter(o -> o.getCustomer().getId().equals(customerId))
                .sorted((o1, o2) -> o2.getOrderId().compareTo(o1.getOrderId()))
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Add item to order
    public OrderItemResponseDto addItemToOrder(Long orderId, OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));

        Product product = productService.getProductEntityById(orderItemRequestDto.getProductId());

        // Check stock
        if (product.getStocks() < orderItemRequestDto.getQuantity()) {
            throw InvalidOrderException.insufficientStock(product.getName(),
                    orderItemRequestDto.getQuantity(), product.getStocks());
        }

        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemRequestDto.getQuantity());

        // Reduce product stock
        productService.reduceStock(orderItemRequestDto.getProductId(), orderItemRequestDto.getQuantity());

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toResponseDto(savedOrderItem);
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
    public OrderItemResponseDto updateOrderItemQuantity(Long orderItemId, int newQuantity) {
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
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toResponseDto(updatedOrderItem);
    }

    // Get order items
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));
        return orderItemMapper.toResponseDtoList(order.getOrderItems());
    }

    // Calculate order total
    public OrderTotalResponseDto getOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));

        Double total = order.getOrderItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        return new OrderTotalResponseDto(orderId, total);
    }

    // Delete order
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> ResourceNotFoundException.orderNotFound(orderId));

        // Restore all stock for items in this order
        for (OrderItem item : order.getOrderItems()) {
            productService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }

        orderRepository.delete(order);
    }

    // Custom Query Methods

    // Get total spent by a customer
    public Double getTotalSpentByCustomer(Long customerId) {
        // Verify customer exists
        customerService.getCustomerEntityById(customerId);
        return orderRepository.getTotalSpentByCustomer(customerId);
    }

    // Get high-value orders above threshold
    public List<OrderResponseDto> getHighValueOrders(Double threshold) {
        return orderRepository.findHighValueOrders(threshold).stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // Count orders for a customer
    public long countOrdersByCustomer(Long customerId) {
        // Verify customer exists
        customerService.getCustomerEntityById(customerId);
        return orderRepository.countOrdersByCustomer(customerId);
    }

    // Get average order item value for a customer
    public Double getAverageOrderItemValueByCustomer(Long customerId) {
        // Verify customer exists
        customerService.getCustomerEntityById(customerId);
        return orderRepository.getAverageOrderItemValueByCustomer(customerId);
    }

    // Find empty orders (orders with no items)
    public List<OrderResponseDto> getEmptyOrders() {
        return orderRepository.findEmptyOrders().stream()
                .map(orderMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
