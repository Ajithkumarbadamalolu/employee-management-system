package com.javaproject.ems.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.ems.dto.EmployeeDto;
import com.javaproject.ems.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // New Import
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Replaces @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // Arrange
        EmployeeDto employeeDto = new EmployeeDto(1L, "John", "Doe", "john@gmail.com");
        given(employeeService.createEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(employeeDto);

        // Act
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

        // Assert
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(employeeDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employeeDto.getLastName()))
                .andExpect(jsonPath("$.email").value(employeeDto.getEmail()));
    }
}