package com.teddybite.entity.types;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RemarkTest {

    @Test
    void testToString() {
        assertEquals("EXTRA LETTUCE", Remark.EXTRA_LETTUCE.toString());
        assertEquals("NO ICE", Remark.NO_ICE.toString());
    }
    
    @Test
    void testValueOf() {
        assertEquals(Remark.LESS_ICE, Remark.valueOf("LESS_ICE"));
    }
}