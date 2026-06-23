package org.example.productjpa.mapper;

import org.example.productjpa.dto.CustomerRequestDto;
import org.example.productjpa.dto.CustomerResponseDto;
import org.example.productjpa.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto toResponseDto(Customer customer);

    Customer toEntity(CustomerRequestDto customerRequestDto);

    void updateEntityFromDto(CustomerRequestDto customerRequestDto, @MappingTarget Customer customer);
}

