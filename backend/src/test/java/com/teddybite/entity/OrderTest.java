package com.teddybite.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class OrderTest {

    private Order createValidOrder() {
        Order order = new Order();

        LocalDateTime now = LocalDateTime.now();

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem());

        order.setOrderId("order-001");
        order.setOrderItems(orderItems);
        order.setPaymentId("pay-001");
        order.setCreatedAt(now);

        return order;
    }

    @Test
    void testGetterSetters() {
        Order order = new Order();

        order.setOrderId("order-001");
        assertEquals("order-001", order.getOrderId());

        order.setPaymentId("pay-001");
        assertEquals("pay-001", order.getPaymentId());

        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);
        assertEquals(now, order.getCreatedAt());

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem());

        order.setOrderItems(orderItems);
        assertEquals(orderItems, order.getOrderItems());
    }

    @Test
    void testConstructors() {
        Order o1 = new Order();
        assertNotNull(o1);
    }

    @Test
    void testCanEqual() {
        Order o1 = createValidOrder();
        Order o2 = createValidOrder();

        assertTrue(o1.canEqual(o2));
    }

    @Test
    void testHashCodeAndEquals() {
        EqualsVerifier.simple().forClass(Order.class).verify();
    }

    @Test
    void testToString() {
        Order o1 = createValidOrder();
        assertTrue(o1.toString().contains("order-001"));
    }
}
