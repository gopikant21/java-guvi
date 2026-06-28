package org.example.productjwt.mapper;

import org.example.productjwt.dto.ProductRequestDto;
import org.example.productjwt.dto.ProductResponseDto;
import org.example.productjwt.model.Product;
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

