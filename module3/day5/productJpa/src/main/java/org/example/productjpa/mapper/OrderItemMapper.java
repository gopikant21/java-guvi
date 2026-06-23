package org.example.productjpa.mapper;

import org.example.productjpa.dto.OrderItemResponseDto;
import org.example.productjpa.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productPrice", source = "product.price")
    @Mapping(target = "lineTotal", expression = "java(orderItem.getProduct().getPrice() * orderItem.getQuantity())")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);

    List<OrderItemResponseDto> toResponseDtoList(List<OrderItem> orderItems);
}

