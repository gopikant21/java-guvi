package org.example.productspringsecurity.mapper;

import org.example.productspringsecurity.dto.CustomerRequestDto;
import org.example.productspringsecurity.dto.CustomerResponseDto;
import org.example.productspringsecurity.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponseDto toResponseDto(Customer customer);

    Customer toEntity(CustomerRequestDto customerRequestDto);

    void updateEntityFromDto(CustomerRequestDto customerRequestDto, @MappingTarget Customer customer);
}

