package com.teddybite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.dto.EmployeeCreateDTO;
import com.teddybite.dto.EmployeeUpdateDTO;
import com.teddybite.entity.Employee;
import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;
import com.teddybite.service.interfaceService.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setEmployeeID("emp-001");
        sampleEmployee.setPassword("ABCD1234");
        sampleEmployee.setName("John Doe");
        sampleEmployee.setGender(Gender.MALE);
        sampleEmployee.setDob(LocalDateTime.now());
        sampleEmployee.setContactNo("0123456789");
        sampleEmployee.setDateJoined(LocalDateTime.now());
        sampleEmployee.setEmail("johndoe@teddybite.com");
        sampleEmployee.setPosition(EmployeePosition.MANAGER);
        sampleEmployee.setSalary(3000.00);
    }

    @Test
    void testDeleteBatch() throws Exception {
        List<String> ids = Arrays.asList("emp-001", "emp-002");
        
        doNothing().when(employeeService).deleteEmployees(anyList());

        mockMvc.perform(delete("/api/employees/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNoContent());
        
        verify(employeeService, times(1)).deleteEmployees(anyList());
    }

    @Test
    void testDeleteById_Success() throws Exception {
        when(employeeService.readEmployeeById("emp-001")).thenReturn(Optional.of(sampleEmployee));
        doNothing().when(employeeService).deleteEmployeeById("emp-001");

        mockMvc.perform(delete("/api/employees/{id}", "emp-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteById_NotFound() throws Exception {
        when(employeeService.readEmployeeById("emp-999")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/employees/{id}", "emp-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll() throws Exception {
        List<Employee> employees = Arrays.asList(sampleEmployee);
        
        when(employeeService.readEmployeeAll()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));
    }

    @Test
    void testFindById_Found() throws Exception {
        when(employeeService.readEmployeeById("emp-001")).thenReturn(Optional.of(sampleEmployee));

        // FIXED: URL changed from /api/employees to /api/employees/{id}
        mockMvc.perform(get("/api/employees/{id}", "emp-001")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(employeeService.readEmployeeById("emp-999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", "emp-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetGenders() throws Exception {
        mockMvc.perform(get("/api/employees/genders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Gender.values().length)));
    }

    @Test
    void testGetPositions() throws Exception {
        mockMvc.perform(get("/api/employees/positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void testSave() throws Exception {
        EmployeeCreateDTO createDTO = new EmployeeCreateDTO();
        createDTO.setName("Jane Doe");
        createDTO.setGender(Gender.FEMALE);
        createDTO.setDob(LocalDateTime.now().minusYears(25)); 
        createDTO.setContactNo("0123456789"); 
        createDTO.setEmail("jane@teddybite.com");
        createDTO.setPosition(EmployeePosition.MANAGER);
        createDTO.setPassword("Teddy@1234");
        
        when(employeeService.createEmployee(any(EmployeeCreateDTO.class))).thenReturn(sampleEmployee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.employeeID", is("emp-001")));
    }

    @Test
    void testUpdateEmployee_Success() throws Exception {
        EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
        updateDTO.setName("John Updated");
        updateDTO.setGender(Gender.MALE);
        updateDTO.setDob(LocalDateTime.now().minusYears(30));
        updateDTO.setContactNo("0198765432");
        updateDTO.setEmail("john.updated@teddybite.com");
        updateDTO.setPosition(EmployeePosition.MANAGER);

        when(employeeService.readEmployeeById("emp-001")).thenReturn(Optional.of(sampleEmployee));

        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmployeeID("emp-001");
        updatedEmployee.setName("John Updated");
        
        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/{id}", "emp-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")));
    }

    @Test
    void testUpdateEmployee_NotFound() throws Exception {
        EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO();
        updateDTO.setName("Ghost User"); 
        updateDTO.setGender(Gender.MALE);
        updateDTO.setDob(LocalDateTime.now().minusYears(20));
        updateDTO.setContactNo("0000000000");
        updateDTO.setEmail("ghost@teddybite.com");
        updateDTO.setPosition(EmployeePosition.MANAGER);
        
        when(employeeService.readEmployeeById("emp-999")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/{id}", "emp-999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }
}