package org.example.productjpa.mapper;

import org.example.productjpa.dto.ProductRequestDto;
import org.example.productjpa.dto.ProductResponseDto;
import org.example.productjpa.model.Product;
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

