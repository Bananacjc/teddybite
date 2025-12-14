package com.teddybite.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

public class LoginDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private LoginDTO createValidDTO() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("john@teddybite.com");
        dto.setPassword("Teddy@1234");
        return dto;
    }
    

    @Test
    void testHashCodeAndHashCode() {
        EqualsVerifier.simple().forClass(LoginDTO.class).verify();
    }

    @Test
    void testCanEqual() {
        LoginDTO dto1 = createValidDTO();
        LoginDTO dto2 = createValidDTO();
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testGetSetEmail() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("update@test.com");
        assertEquals("update@test.com", dto.getEmail());
    }

    @Test
    void testGetSetPassword() {
        LoginDTO dto = new LoginDTO();
        dto.setPassword("Teddy@5678");
        assertEquals("Teddy@5678", dto.getPassword());
    }
    

    @Test
    void testToString() {
        LoginDTO dto = createValidDTO();
        assertTrue(dto.toString().contains("john@teddybite.com"));
    }

    // --- Validation Test ---

    @Test   
    void testEmailValidation() {
        LoginDTO dto = createValidDTO();

        dto.setEmail("");
        assertFalse(validator.validate(dto).isEmpty());
    }

    @Test   
    void testPasswordValidation() {
        LoginDTO dto = createValidDTO();

        dto.setPassword("");
        assertFalse(validator.validate(dto).isEmpty());
    }
}
