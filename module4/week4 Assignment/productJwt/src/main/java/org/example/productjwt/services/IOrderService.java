package org.example.productjwt.services;

import org.example.productjwt.dto.OrderItemRequestDto;
import org.example.productjwt.dto.OrderItemResponseDto;
import org.example.productjwt.dto.OrderResponseDto;
import org.example.productjwt.dto.OrderTotalResponseDto;

import java.util.List;

public interface IOrderService {

    // Create
    OrderResponseDto createOrder(Long customerId);

    // Read
    OrderResponseDto getOrderById(Long orderId);

    List<OrderResponseDto> getAllOrders();

    List<OrderResponseDto> getCustomerOrders(Long customerId);

    List<OrderResponseDto> getCustomerOrdersSortedByNewest(Long customerId);

    // Order Items Management
    OrderItemResponseDto addItemToOrder(Long orderId, OrderItemRequestDto orderItemRequestDto);

    void removeItemFromOrder(Long orderItemId);

    OrderItemResponseDto updateOrderItemQuantity(Long orderItemId, int newQuantity);

    List<OrderItemResponseDto> getOrderItems(Long orderId);

    // Order Calculations
    OrderTotalResponseDto getOrderTotal(Long orderId);

    // Delete
    void cancelOrder(Long orderId);

    // Custom Query Methods - Customer Analytics
    Double getTotalSpentByCustomer(Long customerId);

    List<OrderResponseDto> getHighValueOrders(Double threshold);

    long countOrdersByCustomer(Long customerId);

    Double getAverageOrderItemValueByCustomer(Long customerId);

    List<OrderResponseDto> getEmptyOrders();
}
