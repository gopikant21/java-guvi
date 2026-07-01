package org.example.integrationtesting.service;

import org.example.integrationtesting.dto.EmployeeRequestDto;
import org.example.integrationtesting.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);

    EmployeeResponseDto getEmployeeById(Long id);

    List<EmployeeResponseDto> getAllEmployees();

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto);

    void deleteEmployee(Long id);
}

