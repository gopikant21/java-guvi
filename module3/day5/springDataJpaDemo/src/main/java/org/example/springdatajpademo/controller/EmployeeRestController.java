package org.example.springdatajpademo.controller;

import org.example.springdatajpademo.dto.EmployeeDTO;
import org.example.springdatajpademo.model.Employee;
import org.example.springdatajpademo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/{eid}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long eid) {
        return new ResponseEntity<>(employeeService.getEmployeeById(eid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{eid}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long eid, @RequestBody Employee employeeDetails) {
        return new ResponseEntity<>(employeeService.updateEmployee(eid, employeeDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{eid}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long eid) {
        return new ResponseEntity<>(employeeService.deleteEmployee(eid), HttpStatus.OK);
    }

    // New custom query APIs
    @GetMapping("/search/by-name/{name}")
    public ResponseEntity<List<Employee>> getEmployeesByName(@PathVariable String name) {
        return new ResponseEntity<>(employeeService.getEmployeesByName(name), HttpStatus.OK);
    }

    // Update email using custom query
    @PutMapping("/{eid}/email")
    public ResponseEntity<String> updateEmployeeEmail(@PathVariable Long eid, @RequestParam String email) {
        long result = employeeService.updateEmployeeEmailById(eid, email);
        if (result > 0) {
            return new ResponseEntity<>("Employee email updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
    }

    // Update name using custom query
    @PutMapping("/{eid}/name")
    public ResponseEntity<String> updateEmployeeName(@PathVariable Long eid, @RequestParam String name) {
        long result = employeeService.updateEmployeeNameById(eid, name);
        if (result > 0) {
            return new ResponseEntity<>("Employee name updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
    }

    // Get all employees as DTO
    @GetMapping("/dto/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployeesAsDTO() {
        return new ResponseEntity<>(employeeService.getAllEmployeesAsDTO(), HttpStatus.OK);
    }

    // Get employee by id as DTO
    @GetMapping("/dto/{eid}")
    public ResponseEntity<EmployeeDTO> getEmployeeByIdAsDTO(@PathVariable Long eid) {
        return new ResponseEntity<>(employeeService.getEmployeeByIdAsDTO(eid), HttpStatus.OK);
    }
}
