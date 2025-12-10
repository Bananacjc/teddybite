package com.teddybite.entity.remark;

import java.util.HashMap;
import java.util.Map;

import com.teddybite.entity.types.ItemCategory;

public class RemarkFactory {
    
    private static final Map<ItemCategory, IRemark> strategies = new HashMap<>();

    static {
        strategies.put(ItemCategory.BURGER, new BurgerRemark());
        strategies.put(ItemCategory.BEVERAGES, new BeverageRemark());
    }

    public static IRemark getStrategy(ItemCategory itemCategory) {
        return strategies.getOrDefault(itemCategory, (item, remark) -> {
            throw new IllegalArgumentException("No remarks allowed for category: " + itemCategory.toString());
        });
    }
}
