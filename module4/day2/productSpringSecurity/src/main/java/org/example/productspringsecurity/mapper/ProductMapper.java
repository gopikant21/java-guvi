package org.example.productspringsecurity.mapper;

import org.example.productspringsecurity.dto.ProductRequestDto;
import org.example.productspringsecurity.dto.ProductResponseDto;
import org.example.productspringsecurity.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toResponseDto(Product product);

    List<ProductResponseDto> toResponseDtoList(List<Product> products);

    Product toEntity(ProductRequestDto productRequestDto);

    void updateEntityFromDto(ProductRequestDto productRequestDto, @MappingTarget Product product);
}

