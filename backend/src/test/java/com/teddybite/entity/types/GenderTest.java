package com.teddybite.entity.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenderTest {
    @Test
    void testEnumValues() {
        assertEquals(Gender.MALE, Gender.valueOf("MALE"));
        assertEquals(4, Gender.values().length);
    }
}