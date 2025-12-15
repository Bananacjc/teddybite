package com.teddybite.entity.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemCategoryTest {

    @Test
    void testToString() {
        assertEquals("Burger", ItemCategory.BURGER.toString());
        assertEquals("Fried Chicken", ItemCategory.FRIED_CHICKEN.toString());
        assertEquals("Beverages", ItemCategory.BEVERAGES.toString());
    }

    @Test
    void testEnumConstants() {
        assertNotNull(ItemCategory.valueOf("BURGER"));
        assertEquals(5, ItemCategory.values().length);
    }
}