package com.teddybite.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.teddybite.entity.types.PaymentType;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

public class PaymentDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private PaymentDTO createValidDTO() {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentAmount(3000.00);
        dto.setPaymentType(PaymentType.CASH);
        return dto;
    }

    @Test
    void testHashCodeAndHashCode() {
        EqualsVerifier.simple().forClass(PaymentDTO.class).verify();
    }

    @Test
    void testCanEqual() {
        PaymentDTO dto1 = createValidDTO();
        PaymentDTO dto2 = createValidDTO();
        assertTrue(dto1.canEqual(dto2));
    }

    @Test
    void testGetSetPaymentAmount() {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentAmount(3000.00);
        assertEquals(3000.00, dto.getPaymentAmount());
    }

    @Test
    void testGetSetPaymentType() {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentType(PaymentType.CASH);
        assertEquals(PaymentType.CASH, dto.getPaymentType());
    
    }

    @Test
    void testToString() {
        PaymentDTO dto = createValidDTO();
        assertTrue(dto.toString().contains(PaymentType.CASH.toString()));
    }

    // --- Validation Test ---

    @Test   
    void testValidation_NotNull() {
        PaymentDTO dto = createValidDTO();

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test   
    void testPaymentAmountValidation_Min() {
        PaymentDTO dto = createValidDTO();

        dto.setPaymentAmount(-100);
        assertFalse(validator.validate(dto).isEmpty());
    }



}
