package com.teddybite.dto;
import java.util.List;




public class OrderRequest {
    private String itemCode;     
    private int quantity;        
    private List<String> remarks;  

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public List<String> getRemarks() { return remarks; }
    public void setRemarks(List<String> remarks) { this.remarks = remarks; }
} 

