package com.teddybite.dto;

import com.teddybite.entity.OrderItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testHashCodeAndHashCode() {
        EqualsVerifier.simple().forClass(OrderDTO.class).verify();
    }

    @Test
    void testValidOrderDTO() {
        OrderDTO dto = new OrderDTO();
        dto.setPaymentId("pay-123");
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem()); 
        dto.setOrderItems(items);

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPaymentId() {
        OrderDTO dto = new OrderDTO();
        dto.setPaymentId(""); 
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem());
        dto.setOrderItems(items);

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Payment ID is required", violations.iterator().next().getMessage());
    }

    @Test
    void testEmptyOrderItems() {
        OrderDTO dto = new OrderDTO();
        dto.setPaymentId("pay-123");
        dto.setOrderItems(new ArrayList<>()); 

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("Order items is required", violations.iterator().next().getMessage());
    }
}