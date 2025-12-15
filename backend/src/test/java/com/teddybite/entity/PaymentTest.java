package com.teddybite.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.teddybite.entity.types.PaymentType;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PaymentTest {

    private Payment createValidPayment() {
        Payment payment = new Payment();

        payment.setPaymentId("pay-001");
        payment.setPaymentAmount(100.0);
        payment.setPaymentType(PaymentType.CASH);

        return payment;
    }

    @Test
    void testGettersSetters() {
        Payment payment = new Payment();

        payment.setPaymentId("pay-001");
        assertEquals("pay-001", payment.getPaymentId());

        payment.setPaymentAmount(100.0);
        assertEquals(100.0, payment.getPaymentAmount());

        payment.setPaymentType(PaymentType.CASH);
        assertEquals(PaymentType.CASH, payment.getPaymentType());
    }

    @Test
    void testConstructors() {
        Payment p1 = new Payment();
        assertNotNull(p1);;
    }

        @Test
    void testCanEqual() {
        Payment p1 = createValidPayment();
        Payment p2 = createValidPayment();
        assertTrue(p1.canEqual(p2));
    }

    @Test
    void testHashCodeAndEquals() {
        EqualsVerifier.simple().forClass(Payment.class).verify();
    }

    @Test
    void testToString() {
        Payment p1 = createValidPayment();
        assertTrue(p1.toString().contains("pay-001"));
    }
}
