package org.example.productjwt.mapper;

import org.example.productjwt.dto.CustomerRequestDto;
import org.example.productjwt.dto.CustomerResponseDto;
import org.example.productjwt.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto toResponseDto(Customer customer);

    Customer toEntity(CustomerRequestDto customerRequestDto);

    void updateEntityFromDto(CustomerRequestDto customerRequestDto, @MappingTarget Customer customer);
}

