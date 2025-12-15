package com.teddybite.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;

import nl.jqno.equalsverifier.EqualsVerifier;

public class EmployeeTest {

    private Employee createValidEmployee() {
        Employee employee = new Employee();

        LocalDateTime now = LocalDateTime.now();

        employee.setEmployeeID("emp-001");
        employee.setName("John Doe");
        employee.setPassword("Teddybite@1234");
        employee.setGender(Gender.MALE);
        employee.setDob(now);
        employee.setContactNo("0123456789");
        employee.setEmail("test@email.com");
        employee.setDateJoined(now);
        employee.setPosition(EmployeePosition.MANAGER);
        employee.setSalary(5000.0);
        return employee;
    }

    @Test
    void testGetterSetters() {
        Employee employee = new Employee();

        employee.setEmployeeID("emp-001");
        assertEquals("emp-001", employee.getEmployeeID());

        employee.setName("John Doe");
        assertEquals("John Doe", employee.getName());

        employee.setPassword("Teddybite@1234");
        assertEquals("Teddybite@1234", employee.getPassword());

        employee.setGender(Gender.MALE);
        assertEquals(Gender.MALE, employee.getGender());

        LocalDateTime now = LocalDateTime.now();
        employee.setDob(now);
        assertEquals(now, employee.getDob());

        employee.setContactNo("0123456789");
        assertEquals("0123456789", employee.getContactNo());

        employee.setEmail("test@email.com");
        assertEquals("test@email.com", employee.getEmail());

        employee.setDateJoined(now);
        assertEquals(now, employee.getDateJoined());

        employee.setPosition(EmployeePosition.MANAGER);
        assertEquals(EmployeePosition.MANAGER, employee.getPosition());

        employee.setSalary(5000.0);
        assertEquals(5000.0, employee.getSalary());
    }

    @Test
    void testConstructors() {
        Employee e1 = new Employee();
        assertNotNull(e1);  

        LocalDateTime now = LocalDateTime.now();

        Employee e2 = new Employee(
            "emp-001", 
            "Teddybite@1234", 
            "John Doe", 
            Gender.MALE, 
            now, 
            "0123456789", 
            "test@email.com",
            now, 
            EmployeePosition.MANAGER, 
            5000.0);

        assertEquals("emp-001", e2.getEmployeeID());
        assertEquals("John Doe", e2.getName());
    }

    @Test
    void testCanEqual() {
        Employee e1 = createValidEmployee();
        Employee e2 = createValidEmployee();
        assertTrue(e1.canEqual(e2));
    }

    @Test
    void testHashCodeAndEquals() {
        EqualsVerifier.simple().forClass(Employee.class).verify();
    }

    @Test
    void testToString() {
        Employee e1 = createValidEmployee();
        assertTrue(e1.toString().contains("John Doe"));
    }

    
}
