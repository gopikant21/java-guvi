package org.example.integrationtesting.service;

import org.example.integrationtesting.dto.EmployeeRequestDto;
import org.example.integrationtesting.dto.EmployeeResponseDto;
import org.example.integrationtesting.exception.DuplicateResourceException;
import org.example.integrationtesting.exception.ResourceNotFoundException;
import org.example.integrationtesting.model.Employee;
import org.example.integrationtesting.repository.EmployeeRepository;
import org.example.integrationtesting.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeService Tests")
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequestDto requestDto;
    private Employee employee;
    private EmployeeResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = EmployeeRequestDto.builder()
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        responseDto = EmployeeResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();
    }

    // ============ CREATE EMPLOYEE TESTS ============

    @Test
    @DisplayName("Should create employee successfully when email is unique")
    void testCreateEmployee_Success() {
        // Arrange
        when(employeeRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        EmployeeResponseDto result = employeeService.createEmployee(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(BigDecimal.valueOf(75000.00), result.getSalary());

        verify(employeeRepository, times(1)).existsByEmail(requestDto.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void testCreateEmployee_DuplicateEmail() {
        // Arrange
        when(employeeRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // Act & Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> employeeService.createEmployee(requestDto)
        );

        assertEquals("Employee with email already exists: john@example.com", exception.getMessage());
        verify(employeeRepository, times(1)).existsByEmail(requestDto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    // ============ GET EMPLOYEE BY ID TESTS ============

    @Test
    @DisplayName("Should retrieve employee by id successfully")
    void testGetEmployeeById_Success() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        EmployeeResponseDto result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when employee id does not exist")
    void testGetEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99L)
        );

        assertEquals("Employee not found with id: 99", exception.getMessage());
        verify(employeeRepository, times(1)).findById(99L);
    }

    // ============ GET ALL EMPLOYEES TESTS ============

    @Test
    @DisplayName("Should retrieve all employees successfully")
    void testGetAllEmployees_Success() {
        // Arrange
        Employee employee2 = Employee.builder()
                .id(2L)
                .name("Jane Smith")
                .email("jane@example.com")
                .department("HR")
                .salary(BigDecimal.valueOf(65000.00))
                .build();

        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no employees exist")
    void testGetAllEmployees_EmptyList() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(List.of());

        // Act
        List<EmployeeResponseDto> result = employeeService.getAllEmployees();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employeeRepository, times(1)).findAll();
    }

    // ============ UPDATE EMPLOYEE TESTS ============

    @Test
    @DisplayName("Should update employee successfully")
    void testUpdateEmployee_Success() {
        // Arrange
        EmployeeRequestDto updateDto = EmployeeRequestDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .department("Senior Engineering")
                .salary(BigDecimal.valueOf(85000.00))
                .build();

        Employee existingEmployee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .name("John Updated")
                .email("john.updated@example.com")
                .department("Senior Engineering")
                .salary(BigDecimal.valueOf(85000.00))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        EmployeeResponseDto result = employeeService.updateEmployee(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Updated", result.getName());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals("Senior Engineering", result.getDepartment());
        assertEquals(BigDecimal.valueOf(85000.00), result.getSalary());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findByEmail(updateDto.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent employee")
    void testUpdateEmployee_NotFound() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(99L, requestDto)
        );

        assertEquals("Employee not found with id: 99", exception.getMessage());
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when update email is already taken by another employee")
    void testUpdateEmployee_DuplicateEmail() {
        // Arrange
        Employee anotherEmployee = Employee.builder()
                .id(2L)
                .name("Another Person")
                .email("john.updated@example.com")
                .department("Sales")
                .salary(BigDecimal.valueOf(60000.00))
                .build();

        EmployeeRequestDto updateDto = EmployeeRequestDto.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .department("Senior Engineering")
                .salary(BigDecimal.valueOf(85000.00))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(anotherEmployee));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> employeeService.updateEmployee(1L, updateDto)
        );

        assertEquals("Another employee already uses email: john.updated@example.com", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findByEmail(updateDto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow update with same email for same employee")
    void testUpdateEmployee_SameEmail() {
        // Arrange
        EmployeeRequestDto updateDto = EmployeeRequestDto.builder()
                .name("John Updated")
                .email("john@example.com") // same email
                .department("Senior Engineering")
                .salary(BigDecimal.valueOf(85000.00))
                .build();

        Employee existingEmployee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .name("John Updated")
                .email("john@example.com")
                .department("Senior Engineering")
                .salary(BigDecimal.valueOf(85000.00))
                .build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        EmployeeResponseDto result = employeeService.updateEmployee(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.getEmail());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).findByEmail(updateDto.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // ============ DELETE EMPLOYEE TESTS ============

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee_Success() {
        // Arrange
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));

        // Assert
        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent employee")
    void testDeleteEmployee_NotFound() {
        // Arrange
        when(employeeRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(99L)
        );

        assertEquals("Employee not found with id: 99", exception.getMessage());
        verify(employeeRepository, times(1)).existsById(99L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
