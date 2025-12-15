package com.teddybite.entity.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeePositionTest {

    @Test
    void testSalaries() {
        assertEquals(4500f, EmployeePosition.MANAGER.salary);
        assertEquals(3800f, EmployeePosition.CASHIER.salary);
        assertEquals(3500f, EmployeePosition.KITCHEN_CREW.salary);
        assertEquals(3000f, EmployeePosition.OTHER.salary);
    }

    @Test
    void testValues() {
        EmployeePosition[] positions = EmployeePosition.values();
        assertTrue(positions.length > 0);
    }

    @Test
    void testValueOf() {
        assertEquals(EmployeePosition.MANAGER, EmployeePosition.valueOf("MANAGER"));
    }
}