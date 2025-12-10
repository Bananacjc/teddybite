package com.teddybite.service.interfaceService;

import java.util.List;
import java.util.Optional;

import com.teddybite.entity.Item;

public interface IItemService {
    
    public Item createItem(Item item);
    public List<Item> readItemAll();
    public Optional<Item> readItemById(String id);
    public Item updateItem(Item item);
    public void deleteItemById(String id);

}
