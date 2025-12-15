package com.teddybite.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.teddybite.entity.types.ItemCategory;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ItemTest {

    private Item createValidItem() {
        Item item = new Item();

        item.setItemId("item-001");
        item.setItemName("Item Name");
        item.setItemPrice(150.00);
        item.setItemCategory(ItemCategory.BEVERAGES);
        item.setItemImage("https://img");

        return item;
    }

    @Test
    void testGetterSetters() {
        Item item = new Item();

        item.setItemId("item-001");
        assertEquals("item-001", item.getItemId());

        item.setItemName("Item Name");
        assertEquals("Item Name", item.getItemName());

        item.setItemPrice(150.0);
        assertEquals(150.0, item.getItemPrice());

        item.setItemCategory(ItemCategory.BEVERAGES);
        assertEquals(ItemCategory.BEVERAGES, item.getItemCategory());

        item.setItemImage("https://img");
        assertEquals("https://img", item.getItemImage());
    }

    @Test
    void testConstructors() {
        Item i1 = new Item();
        assertNotNull(i1);

        Item i2 = new Item(
                "item-001",
                "Item Name",
                150.00,
                ItemCategory.BEVERAGES,
                "http://img");

        assertEquals("item-001", i2.getItemId());
        assertEquals("Item Name", i2.getItemName());

    }

    @Test
    void testCanEqual() {
        Item i1 = createValidItem();
        Item i2 = createValidItem();

        assertTrue(i1.canEqual(i2));
    }

    @Test
    void testHashCodeAndEquals() {
        EqualsVerifier.simple().forClass(Item.class).verify();
    }

    @Test
    void testToString() {
        Item i1 = createValidItem();
        assertTrue(i1.toString().contains("Item Name"));
    }
}
