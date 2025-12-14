package com.teddybite.dto;

import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeUpdateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmployeeUpdateDTO createValidDTO() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setName("Jane Doe");
        dto.setGender(Gender.FEMALE);
        dto.setDob(LocalDateTime.now().minusYears(30));
        dto.setContactNo("0987654321");
        dto.setEmail("jane@teddybite.com");
        dto.setPosition(EmployeePosition.MANAGER);
        return dto;
    }

    @Test
    void testCanEqual() {
        EmployeeUpdateDTO dto1 = createValidDTO();
        EmployeeUpdateDTO dto2 = createValidDTO();
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testHashCodeAndHashCode() {
        EqualsVerifier.simple().forClass(EmployeeUpdateDTO.class).verify();
    }

    @Test
    void testToString() {
        EmployeeUpdateDTO dto = createValidDTO();
        assertTrue(dto.toString().contains("Jane Doe"));
    }

    @Test
    void testGetSetName() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setName("New Name");
        assertEquals("New Name", dto.getName());
    }

    @Test
    void testGetSetGender() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setGender(Gender.MALE);
        assertEquals(Gender.MALE, dto.getGender());
    }

    @Test
    void testGetSetDob() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        LocalDateTime date = LocalDateTime.now();
        dto.setDob(date);
        assertEquals(date, dto.getDob());
    }

    @Test
    void testGetSetContactNo() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setContactNo("1112223333");
        assertEquals("1112223333", dto.getContactNo());
    }

    @Test
    void testGetSetEmail() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setEmail("update@test.com");
        assertEquals("update@test.com", dto.getEmail());
    }

    @Test
    void testGetSetPosition() {
        EmployeeUpdateDTO dto = new EmployeeUpdateDTO();
        dto.setPosition(EmployeePosition.MANAGER);
        assertEquals(EmployeePosition.MANAGER, dto.getPosition());
    }

    // --- Validation Tests ---

    @Test
    void testValidationConstraints() {
        EmployeeUpdateDTO dto = createValidDTO();
        
        // Valid State
        assertTrue(validator.validate(dto).isEmpty());

        // Invalid Email
        dto.setEmail("not-valid");
        assertEquals(1, validator.validate(dto).size());
    }
}