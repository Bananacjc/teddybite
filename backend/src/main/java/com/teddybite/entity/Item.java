package com.teddybite.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teddybite.entity.types.ItemCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
public class Item {
    @Id
    private String itemId;
    private String itemName;
    private double itemPrice;
    private ItemCategory itemCategory;
    private String itemImage;
}
