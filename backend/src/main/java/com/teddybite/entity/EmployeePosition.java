package com.teddybite.entity;

public enum EmployeePosition {
    MANAGER(4500f),
    CASHIER(3800f),
    CHEF(3500f),
    WAITER(3000f),
    CLEANER(2800f);

    public final double salary;

    private EmployeePosition(double salary) {
        this.salary = salary;
    }
}