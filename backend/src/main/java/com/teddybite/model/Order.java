package com.teddybite.model;

import java.util.List;
import com.teddybite.dto.OrderRequest;

public class Order {
    private String id;
    private List<OrderRequest> items;
    private String status;

    public Order() {}

    public Order(String id, List<OrderRequest> items, String status) {
        this.id = id;
        this.items = items;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<OrderRequest> getItems() { return items; }
    public void setItems(List<OrderRequest> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
