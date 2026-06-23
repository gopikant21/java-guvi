package org.example.productjpa.services;

import org.example.productjpa.dto.OrderItemResponseDto;
import org.example.productjpa.dto.OrderItemRequestDto;
import org.example.productjpa.dto.OrderResponseDto;
import org.example.productjpa.dto.OrderTotalResponseDto;
import org.example.productjpa.exceptions.InvalidOrderException;
import org.example.productjpa.exceptions.ResourceNotFoundException;
import org.example.productjpa.mapper.OrderItemMapper;
import org.example.productjpa.mapper.OrderMapper;
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
import java.util.stream.Collectors;

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
}



