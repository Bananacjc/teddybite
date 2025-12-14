package com.teddybite.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.teddybite.dto.EmployeeCreateDTO;
import com.teddybite.entity.Employee;
import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;
import com.teddybite.exception.DuplicateResourceException;
import com.teddybite.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee sampleEmployee;
    private EmployeeCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        sampleEmployee = new Employee();
        sampleEmployee.setEmployeeID("emp-001");
        sampleEmployee.setName("John Doe");
        sampleEmployee.setEmail("john@teddybite.com");
        sampleEmployee.setContactNo("0123456789");
        sampleEmployee.setPosition(EmployeePosition.MANAGER);
        sampleEmployee.setSalary(3000.0);

        createDTO = new EmployeeCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("john@teddybite.com");
        createDTO.setContactNo("0123456789");
        createDTO.setGender(Gender.MALE);
        createDTO.setDob(LocalDateTime.now().minusYears(25));
        createDTO.setPosition(EmployeePosition.MANAGER);
        createDTO.setPassword("Password123!");
    }

    @Test
    void testCreateEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(employeeRepository.existsByContactNo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        Employee result = employeeService.createEmployee(createDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());

        verify(passwordEncoder).encode("Password123!");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmail(createDTO.getEmail())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            employeeService.createEmployee(createDTO);
        });

        assertTrue(exception.getMessage().contains("Email already exists"));
        
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_DuplicateContact() {
        when(employeeRepository.existsByEmail(createDTO.getEmail())).thenReturn(false);
        when(employeeRepository.existsByContactNo(createDTO.getContactNo())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            employeeService.createEmployee(createDTO);
        });

        assertTrue(exception.getMessage().contains("Contact number already exists"));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testReadEmployeeAll() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sampleEmployee));

        List<Employee> result = employeeService.readEmployeeAll();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testReadEmployeeById_Found() {
        when(employeeRepository.findById("emp-001")).thenReturn(Optional.of(sampleEmployee));

        Optional<Employee> result = employeeService.readEmployeeById("emp-001");

        assertTrue(result.isPresent());
        assertEquals("emp-001", result.get().getEmployeeID());
    }

    @Test
    void testReadEmployeeById_NotFound() {
        when(employeeRepository.findById("emp-999")).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.readEmployeeById("emp-999");

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateEmployee_Success() {
        // Mock checks returning false (no duplicates)
        when(employeeRepository.existsByEmailAndEmployeeIDNot(anyString(), anyString())).thenReturn(false);
        when(employeeRepository.existsByContactNoAndEmployeeIDNot(anyString(), anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(sampleEmployee);

        Employee result = employeeService.updateEmployee(sampleEmployee);

        assertNotNull(result);
        verify(employeeRepository).save(sampleEmployee);
    }

    @Test
    void testUpdateEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmailAndEmployeeIDNot(sampleEmployee.getEmail(), sampleEmployee.getEmployeeID()))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            employeeService.updateEmployee(sampleEmployee);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_DuplicateContact() {
        when(employeeRepository.existsByEmailAndEmployeeIDNot(anyString(), anyString())).thenReturn(false);
        when(employeeRepository.existsByContactNoAndEmployeeIDNot(sampleEmployee.getContactNo(), sampleEmployee.getEmployeeID()))
                .thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            employeeService.updateEmployee(sampleEmployee);
        });

        assertEquals("Contact number already exists", exception.getMessage());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployeeById() {
        // Since the method is void, we just verify it calls the repo
        doNothing().when(employeeRepository).deleteById("emp-001");

        employeeService.deleteEmployeeById("emp-001");

        verify(employeeRepository, times(1)).deleteById("emp-001");
    }

    @Test
    void testDeleteEmployeesBatch() {
        List<String> ids = Arrays.asList("emp-001", "emp-002");
        doNothing().when(employeeRepository).deleteAllById(ids);

        employeeService.deleteEmployees(ids);

        verify(employeeRepository, times(1)).deleteAllById(ids);
    }


}
