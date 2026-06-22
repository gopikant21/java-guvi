package org.example.springdatajpademo.controller;

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
}
