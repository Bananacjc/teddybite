package com.teddybite.entity.remark;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teddybite.entity.OrderItem;
import com.teddybite.entity.types.Remark;

@ExtendWith(MockitoExtension.class)
public class BeverageRemarkTest {

    private BeverageRemark beverageRemark;

    @Mock
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        beverageRemark = new BeverageRemark();
    }

    @Test
    void testGetAllowedRemarks() {
        List<Remark> allowed = beverageRemark.getAllowedRemarks();

        assertTrue(allowed.contains(Remark.NO_ICE));
        assertTrue(allowed.contains(Remark.LESS_ICE));
        assertEquals(2, allowed.size());

    }

    @Test
    void testApplyRemark_validRemark() {
        beverageRemark.applyRemark(orderItem, Remark.NO_ICE);
        verify(orderItem, times(1)).addRemark(Remark.NO_ICE);

        beverageRemark.applyRemark(orderItem, Remark.LESS_ICE);
        verify(orderItem, times(1)).addRemark(Remark.LESS_ICE);
    }

    @Test
    void testApplyRemark_invalidRemark() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            beverageRemark.applyRemark(orderItem, Remark.EXTRA_CHEESE);
        });

        assertEquals("Invalid remark for Beverage: " + Remark.EXTRA_CHEESE.toString(), exception.getMessage());

        verify(orderItem, never()).addRemark(any());

    }

}
