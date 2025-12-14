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
public class BurgerRemarkTest {

    private BurgerRemark burgerRemark;

    @Mock
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        burgerRemark = new BurgerRemark();
    }

    @Test
    void testGetAllowedRemarks() {
        List<Remark> allowed = burgerRemark.getAllowedRemarks();

        assertTrue(allowed.contains(Remark.EXTRA_CHEESE));
        assertTrue(allowed.contains(Remark.EXTRA_SAUCE));
        assertTrue(allowed.contains(Remark.EXTRA_LETTUCE));
        assertEquals(3, allowed.size());

    }

    @Test
    void testApplyRemark_validRemark() {
        burgerRemark.applyRemark(orderItem, Remark.EXTRA_CHEESE);
        verify(orderItem, times(1)).addRemark(Remark.EXTRA_CHEESE);

        burgerRemark.applyRemark(orderItem, Remark.EXTRA_SAUCE);
        verify(orderItem, times(1)).addRemark(Remark.EXTRA_SAUCE);

        burgerRemark.applyRemark(orderItem, Remark.EXTRA_LETTUCE);
        verify(orderItem, times(1)).addRemark(Remark.EXTRA_LETTUCE);
    }

    @Test
    void testApplyRemark_invalidRemark() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            burgerRemark.applyRemark(orderItem, Remark.NO_ICE);
        });

        assertEquals("Invalid remark for Burger: " + Remark.NO_ICE.toString(), exception.getMessage());

        verify(orderItem, never()).addRemark(any());

    }

}
