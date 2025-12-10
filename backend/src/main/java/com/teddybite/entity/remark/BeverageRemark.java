package com.teddybite.entity.remark;

import java.util.Arrays;
import java.util.List;

import com.teddybite.entity.OrderItem;
import com.teddybite.entity.types.Remark;

public class BeverageRemark implements IRemark {

    private static final List<Remark> ALLOWED_REMARK = Arrays.asList(
            Remark.NO_ICE, Remark.LESS_ICE);

    @Override
    public void applyRemark(OrderItem orderItem, Remark remark) {
        if (ALLOWED_REMARK.contains(remark)) {
            orderItem.addRemark(remark);
        } else {
            throw new IllegalArgumentException("Invalid remark for Beverage: " + remark);
        }

    }

    @Override
    public List<Remark> getAllowedRemarks() {
        return ALLOWED_REMARK;
    }

}
