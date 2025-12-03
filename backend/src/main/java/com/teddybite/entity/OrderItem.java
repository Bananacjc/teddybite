package com.teddybite.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String itemId;
    private String itemName;
    private double unitPrice;
    private int quantity;
    private double lineTotal;
}
