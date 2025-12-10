package com.teddybite.entity.types;

public enum Remark {
    EXTRA_LETTUCE("EXTRA LETTUCE"),
    EXTRA_CHEESE("EXTRA CHEESE"),
    EXTRA_SAUCE("EXTRA SAUCE"),
    NO_ICE("NO ICE"),
    LESS_ICE("LESS ICE");
    
    private String description;

    Remark(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
