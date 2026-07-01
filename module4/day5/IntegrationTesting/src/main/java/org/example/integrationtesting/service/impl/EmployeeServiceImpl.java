package org.example.integrationtesting.service.impl;

import org.example.integrationtesting.dto.EmployeeRequestDto;
import org.example.integrationtesting.dto.EmployeeResponseDto;
import org.example.integrationtesting.exception.DuplicateResourceException;
import org.example.integrationtesting.exception.ResourceNotFoundException;
import org.example.integrationtesting.model.Employee;
import org.example.integrationtesting.repository.EmployeeRepository;
import org.example.integrationtesting.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        if (employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Employee with email already exists: " + requestDto.getEmail());
        }

        Employee employee = Employee.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .department(requestDto.getDepartment())
                .salary(requestDto.getSalary())
                .build();

        Employee saved = employeeRepository.save(employee);
        return toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return toResponseDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.findByEmail(requestDto.getEmail())
                .filter(employee -> !employee.getId().equals(id))
                .ifPresent(employee -> {
                    throw new DuplicateResourceException("Another employee already uses email: " + requestDto.getEmail());
                });

        existing.setName(requestDto.getName());
        existing.setEmail(requestDto.getEmail());
        existing.setDepartment(requestDto.getDepartment());
        existing.setSalary(requestDto.getSalary());

        Employee updated = employeeRepository.save(existing);
        return toResponseDto(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeResponseDto toResponseDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .build();
    }
}

