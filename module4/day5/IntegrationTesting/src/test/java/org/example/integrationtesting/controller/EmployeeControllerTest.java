package org.example.integrationtesting.controller;

import org.example.integrationtesting.dto.EmployeeResponseDto;
import org.example.integrationtesting.exception.GlobalExceptionHandler;
import org.example.integrationtesting.exception.ResourceNotFoundException;
import org.example.integrationtesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService employeeService;

    @Test
    void testGetEmployeeById_Success() throws Exception {
        // Arrange
        EmployeeResponseDto employeeResponseDto = EmployeeResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        when(employeeService.getEmployeeById(1L)).thenReturn(employeeResponseDto);

        // Act & Assert
        mockMvc.perform(get("/api/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(75000.00));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void testEmployeeNotFound() throws Exception {
        when(employeeService.getEmployeeById(10L)).thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/employees/{id}", 10L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found"));


        verify(employeeService).getEmployeeById(10L);
    }

    @Test
    void testDeleteEmployeeById_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/employees/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void testPostEmployeeById_Success() throws Exception {
        // Arrange
        EmployeeResponseDto employeeResponseDto = EmployeeResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .department("Engineering")
                .salary(BigDecimal.valueOf(75000.00))
                .build();

        String requestBody = """
            {
              "name": "John Doe",
              "email": "john@example.com",
              "department": "Engineering",
              "salary": 75000.00
            }
            """;

        when(employeeService.createEmployee(any())).thenReturn(employeeResponseDto);

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(75000.00));

        verify(employeeService).createEmployee(any());
    }

    @Test
    void testPutEmployeeById_Success() throws Exception {
        // Arrange
        EmployeeResponseDto updatedEmployee = EmployeeResponseDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .department("Marketing")
                .salary(BigDecimal.valueOf(80000.00))
                .build();

        when(employeeService.updateEmployee(eq(1L), any())).thenReturn(updatedEmployee);

        // Act & Assert
        mockMvc.perform(put("/api/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Jane Doe",
                              "email": "jane@example.com",
                              "department": "Marketing",
                              "salary": 80000.00
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.department").value("Marketing"))
                .andExpect(jsonPath("$.salary").value(80000.00));

        verify(employeeService).updateEmployee(eq(1L), any());
    }
}
