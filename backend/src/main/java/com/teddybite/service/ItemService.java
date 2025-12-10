package com.teddybite.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.teddybite.entity.Item;
import com.teddybite.repository.ItemRepository;
import com.teddybite.service.interfaceService.IItemService;

@Service
public class ItemService implements IItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item createItem(Item item) {
        Item newItem = new Item();

        newItem.setItemName(item.getItemName());
        newItem.setItemCategory(item.getItemCategory());
        newItem.setItemPrice(item.getItemPrice());

        return itemRepository.save(newItem);
    }

    @Override
    public List<Item> readItemAll() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> readItemById(String id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void deleteItemById(String id) {
        itemRepository.deleteById(id);
    }
}
