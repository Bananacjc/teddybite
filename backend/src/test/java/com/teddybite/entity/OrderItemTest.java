package com.teddybite.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import com.teddybite.entity.types.Remark;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OrderItemTest {

    private OrderItem createValidOrderItem() {

        OrderItem orderItem = new OrderItem();

        orderItem.setItemId("item-001");
        orderItem.setItemName("Item Name");
        orderItem.setUnitPrice(10.0);
        orderItem.setQuantity(10);
        orderItem.setLineTotal(10.0 * 10);

        return orderItem;
    }

    @Test
    void testGetterSetters() {
        OrderItem orderItem = new OrderItem();

        orderItem.setItemId("item-001");
        assertEquals("item-001", orderItem.getItemId());

        orderItem.setItemName("Item Name");
        assertEquals("Item Name", orderItem.getItemName());

        orderItem.setUnitPrice(10.0);
        assertEquals(10.0, orderItem.getUnitPrice());

        orderItem.setQuantity(10);
        assertEquals(10, orderItem.getQuantity());

        orderItem.setLineTotal(10.0 * 10);
        assertEquals(10.0 * 10, orderItem.getLineTotal());
    }

    @Test
    void testConstructors() {
        OrderItem oi1 = new OrderItem();
        assertNotNull(oi1);

        OrderItem oi2 = new OrderItem(
                "item-001",
                "Item Name",
                10.0,
                10,
                10.0 * 10,
                new ArrayList<Remark>());

        assertEquals("item-001", oi2.getItemId());
        assertEquals(10.0, oi2.getUnitPrice());
    }

    @Test
    void testCanEqual() {
        OrderItem oi1 = createValidOrderItem();
        OrderItem oi2 = createValidOrderItem();

        assertTrue(oi1.canEqual(oi2));
    }

    @Test
    void testHashCodeAndEquals() {
        EqualsVerifier.simple().forClass(OrderItem.class).verify();
    }

    @Test
    void testToString() {
        OrderItem oi1 = createValidOrderItem();
        assertTrue(oi1.toString().contains("item-001"));
    }

}
