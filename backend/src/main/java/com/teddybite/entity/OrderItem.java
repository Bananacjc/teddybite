package com.teddybite.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.teddybite.entity.types.Remark;

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

    private List<Remark> remarks = new ArrayList<>();

    public void addRemark(Remark remark) {
        this.remarks.add(remark);
    }
}
