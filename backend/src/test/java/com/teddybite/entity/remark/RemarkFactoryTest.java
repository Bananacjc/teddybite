package com.teddybite.entity.remark;

import com.teddybite.entity.OrderItem;
import com.teddybite.entity.types.ItemCategory;
import com.teddybite.entity.types.Remark;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class RemarkFactoryTest {

    @Mock
    private OrderItem orderItem;

    @Test
    void getStrategy_Burger_returnsBurgerRemark() {
        IRemark strategy = RemarkFactory.getStrategy(ItemCategory.BURGER);
        
        assertNotNull(strategy);
        assertInstanceOf(BurgerRemark.class, strategy);
    }

    @Test
    void getStrategy_Beverage_returnsBeverageRemark() {
        IRemark strategy = RemarkFactory.getStrategy(ItemCategory.BEVERAGES);
        
        assertNotNull(strategy);
        assertInstanceOf(BeverageRemark.class, strategy);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getStrategy_UnknownCategory_returnsDefaultErrorStrategy() throws Exception {
        // 1. Access the private static 'strategies' map via Reflection
        Field field = RemarkFactory.class.getDeclaredField("strategies");
        field.setAccessible(true);
        Map<ItemCategory, IRemark> strategiesMap = (Map<ItemCategory, IRemark>) field.get(null);

        // 2. Temporarily remove a known category (e.g., BURGER) to simulate it being "unknown"
        // We save the original strategy so we can put it back later
        IRemark originalStrategy = strategiesMap.remove(ItemCategory.BURGER);

        try {
            // 3. Now calling getStrategy(BURGER) will fail to find it in the map 
            // and trigger the default "unknown" lambda
            IRemark strategy = RemarkFactory.getStrategy(ItemCategory.BURGER);
            
            assertNotNull(strategy);
            
            // 4. Verify it is NOT the standard BurgerRemark anymore
            assertFalse(strategy instanceof BurgerRemark);

            // 5. Verify the default strategy throws the expected exception
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                strategy.applyRemark(orderItem, Remark.EXTRA_CHEESE);
            });

            // This proves the default lambda was used
            assertTrue(exception.getMessage().contains("No remarks allowed for category"));

        } finally {
            // 6. CRITICAL: Restore the map state! 
            // Since 'strategies' is static, if we don't fix it, other tests will fail.
            if (originalStrategy != null) {
                strategiesMap.put(ItemCategory.BURGER, originalStrategy);
            }
        }
    }
}