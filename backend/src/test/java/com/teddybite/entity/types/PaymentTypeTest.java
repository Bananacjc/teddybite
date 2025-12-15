package com.teddybite.entity.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTypeTest {
    @Test
    void testEnumValues() {
        assertEquals(PaymentType.CASH, PaymentType.valueOf("CASH"));
        assertEquals(PaymentType.CREDIT_CARD, PaymentType.valueOf("CREDIT_CARD"));
    }
}