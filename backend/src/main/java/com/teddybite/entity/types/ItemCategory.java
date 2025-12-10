package com.teddybite.entity.types;

public enum ItemCategory {
    BURGER("Burger"),
    FRIED_CHICKEN("Fried Chicken"),
    BEVERAGES("Beverages"),
    DESSERTS("Desserts"),
    CONDIMENTS("Condiments");

    private String name;

    ItemCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
