package com.teddybite.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DuplicateResourceExceptionTest {

    @Test
    void testExceptionStructure() {
        String field = "email";
        String message = "Email already exists";

        DuplicateResourceException exception = new DuplicateResourceException(field, message);

        assertEquals(field, exception.getField());
        assertEquals(message, exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);

    }
}
