package org.example.springdatajpademo.service;

import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long eid) {
        return employeeRepository.findById(eid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + eid));
    }

    public Employee updateEmployee(Long eid, Employee employeeDetails) {
        Employee employee = getEmployeeById(eid);
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        return employeeRepository.save(employee);
    }

    public String deleteEmployee(Long eid) {
        Employee employee = getEmployeeById(eid);
        employeeRepository.delete(employee);
        return "Employee deleted successfully";
    }
}
