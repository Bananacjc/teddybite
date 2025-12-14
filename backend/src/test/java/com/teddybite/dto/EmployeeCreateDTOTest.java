package com.teddybite.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.teddybite.entity.types.EmployeePosition;
import com.teddybite.entity.types.Gender;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

public class EmployeeCreateDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmployeeCreateDTO createValidDTO() {
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setName("John Doe");
        dto.setGender(Gender.MALE);
        dto.setDob(LocalDateTime.now().minusYears(25));
        dto.setContactNo("0123456789");
        dto.setEmail("john@teddybite.com");
        dto.setPosition(EmployeePosition.MANAGER); // Assuming Enum has values
        dto.setPassword("Teddy@1234");
        return dto;
    }

    @Test
    void testCanEqual() {
        EmployeeCreateDTO dto1 = createValidDTO();
        EmployeeCreateDTO dto2 = createValidDTO();
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testHashCodeAndHashCode() {
        EqualsVerifier.simple().forClass(EmployeeCreateDTO.class).verify();
    }

    @Test
    void testToString() {
        EmployeeCreateDTO dto = createValidDTO();
        assertTrue(dto.toString().contains("John Doe"));
    }

    @Test
    void testGetSetName() {
        EmployeeCreateDTO dto = createValidDTO();
        dto.setName("New Name");
        assertEquals("New Name", dto.getName());
    }

    @Test
    void testGetSetGender() {
        EmployeeCreateDTO dto = createValidDTO();
        dto.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, dto.getGender());
    }

    @Test
    void testGetSetDob() {
        EmployeeCreateDTO dto = createValidDTO();
        LocalDateTime newDate = LocalDateTime.now();
        dto.setDob(newDate);
        assertEquals(newDate, dto.getDob());
    }

    @Test
    void testGetSetContactNo() {
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setContactNo("1112223333");
        assertEquals("1112223333", dto.getContactNo());
    }

    @Test
    void testGetSetEmail() {
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setEmail("update@test.com");
        assertEquals("update@test.com", dto.getEmail());
    }

    @Test
    void testGetSetPosition() {
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setPosition(EmployeePosition.MANAGER);
        assertEquals(EmployeePosition.MANAGER, dto.getPosition());
    }

    @Test
    void testGetSetPassword() {
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setPassword("Teddy@5678");
        assertEquals("Teddy@5678", dto.getPassword());
    }

    // --- Validation ---

    @Test
    void testNameValidation() {
        EmployeeCreateDTO dto = createValidDTO();

        // Null name
        dto.setName(null);
        assertFalse(validator.validate(dto).isEmpty());

        // Invalid pattern
        dto.setName("John123");
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test
    void testPasswordValidation() {
        EmployeeCreateDTO dto = createValidDTO();
        
        // Too short
        dto.setPassword("Weak1!"); 
        Set<ConstraintViolation<EmployeeCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testEmailValidation() {
        EmployeeCreateDTO dto = createValidDTO();

        // Invaluid format
        dto.setEmail("invalid-email");
        assertFalse(validator.validate(dto).isEmpty());
    }

}
