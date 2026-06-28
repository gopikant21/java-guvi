package org.example.productjwt.mapper;

import org.example.productjwt.dto.OrderResponseDto;
import org.example.productjwt.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "total", expression = "java(order.getOrderItems().stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum())")
    OrderResponseDto toResponseDto(Order order);

    List<OrderResponseDto> toResponseDtoList(List<Order> orders);
}

