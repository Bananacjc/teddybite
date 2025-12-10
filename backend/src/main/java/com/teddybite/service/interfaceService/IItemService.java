package com.teddybite.service.interfaceService;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import com.teddybite.entity.Item;

public interface IItemService {

    public Item createItem(Item item, MultipartFile image);

    public List<Item> readItemAll();

    public Optional<Item> readItemById(String id);

    public Item updateItem(Item item, MultipartFile image);

    public void deleteItemById(String id);

}
