package org.example.springdatajpademo.dto;

import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.model.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail()
        );
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        return employee;
    }

    public List<EmployeeDTO> toDTOList(List<Employee> employees) {
        if (employees == null) {
            return new ArrayList<>();
        }
        return employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Employee> toEntityList(List<EmployeeDTO> employeeDTOs) {
        if (employeeDTOs == null) {
            return new ArrayList<>();
        }
        return employeeDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}

