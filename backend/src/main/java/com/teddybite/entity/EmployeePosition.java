package com.teddybite.entity;

public enum EmployeePosition {
    MANAGER(4500f),
    CASHIER(3800f),
    KITCHEN_CREW(3500f),
    OTHER(3000f);

    public final double salary;

    private EmployeePosition(double salary) {
        this.salary = salary;
    }
}