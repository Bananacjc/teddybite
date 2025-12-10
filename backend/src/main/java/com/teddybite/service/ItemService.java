package com.teddybite.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Item createItem(Item item, MultipartFile image) {
        Item newItem = new Item();

        newItem.setItemName(item.getItemName());
        newItem.setItemCategory(item.getItemCategory());
        newItem.setItemPrice(item.getItemPrice());

        if (image != null && !image.isEmpty()) {
            String imageUrl = saveImage(image);
            newItem.setItemImage(imageUrl);
        } else {
            newItem.setItemImage(item.getItemImage()); // Allow setting URL directly if provided
        }

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
    public Item updateItem(Item item, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            // Retrieve existing item from DB to ensure we have the correct old image URL
            Optional<Item> existingItemOpt = itemRepository.findById(item.getItemId());
            if (existingItemOpt.isPresent()) {
                String oldImage = existingItemOpt.get().getItemImage();
                if (oldImage != null && !oldImage.isEmpty()) {
                    deleteImage(oldImage);
                }
            }

            String imageUrl = saveImage(image);
            item.setItemImage(imageUrl);
        }
        return itemRepository.save(item);
    }

    @Override
    public void deleteItemById(String id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            String imageUrl = item.get().getItemImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                deleteImage(imageUrl);
            }
            itemRepository.deleteById(id);
        }
    }

    private void deleteImage(String imageUrl) {
        try {
            // Extract filename from URL
            // Assuming URL format: http://host:port/uploads/filename
            String[] parts = imageUrl.split("/uploads/");
            if (parts.length > 1) {
                String fileName = parts[1];
                java.nio.file.Path path = java.nio.file.Paths.get("uploads").resolve(fileName);
                java.nio.file.Files.deleteIfExists(path);
            }
        } catch (java.io.IOException e) {
            System.err.println("Failed to delete image: " + e.getMessage());
            // Optionally log or handle exception
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String fileName = java.util.UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            java.nio.file.Path path = java.nio.file.Paths.get("uploads");
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createDirectories(path);
            }
            java.nio.file.Files.copy(image.getInputStream(), path.resolve(fileName),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            return org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
