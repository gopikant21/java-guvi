package org.example.springdatajpademo.service;

import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.dto.EmployeeDTO;

import java.util.List;

public interface IEmployeeService {
    Employee addEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long eid);
    Employee updateEmployee(Long eid, Employee employeeDetails);
    String deleteEmployee(Long eid);
    List<Employee> getEmployeesByName(String name);
    long updateEmployeeEmailById(Long eid, String newEmail);
    long updateEmployeeNameById(Long eid, String newName);
    List<EmployeeDTO> getAllEmployeesAsDTO();
    EmployeeDTO getEmployeeByIdAsDTO(Long eid);
}

