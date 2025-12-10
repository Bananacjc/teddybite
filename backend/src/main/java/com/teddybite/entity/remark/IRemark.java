package com.teddybite.entity.remark;

import com.teddybite.entity.OrderItem;
import com.teddybite.entity.types.Remark;

public interface IRemark {
    void applyRemark(OrderItem orderItem, Remark remark);
}
