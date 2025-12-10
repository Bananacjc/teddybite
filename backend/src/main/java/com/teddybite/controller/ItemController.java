package com.teddybite.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.teddybite.entity.Item;
import com.teddybite.service.interfaceService.IItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final IItemService itemService;

    public ItemController(IItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Item> save(@RequestPart("item") @Valid Item item,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        Item savedItem = itemService.createItem(item, image);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.getItemId())
                .toUri();

        return ResponseEntity.created(location).body(savedItem);
    }

    @GetMapping
    public List<Item> findAll() {
        return itemService.readItemAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> findById(@PathVariable String id) {
        return ResponseEntity.of(itemService.readItemById(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Item> updateItem(@PathVariable String id,
            @RequestPart("item") @Valid Item updateItem,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return itemService.readItemById(id)
                .map(currentItem -> {
                    currentItem.setItemName(updateItem.getItemName());
                    currentItem.setItemCategory(updateItem.getItemCategory());
                    currentItem.setItemPrice(updateItem.getItemPrice());
                    if (updateItem.getItemImage() != null) {
                        currentItem.setItemImage(updateItem.getItemImage());
                    }

                    Item updatedItem = itemService.updateItem(currentItem, image);

                    return ResponseEntity.ok(updatedItem);
                })
                .orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        if (itemService.readItemById(id).isPresent()) {
            itemService.deleteItemById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categories")
    public com.teddybite.entity.types.ItemCategory[] getCategories() {
        return com.teddybite.entity.types.ItemCategory.values();
    }

    @GetMapping("/remarks")
    public java.util.Map<com.teddybite.entity.types.ItemCategory, java.util.List<com.teddybite.entity.types.Remark>> getRemarks() {
        java.util.Map<com.teddybite.entity.types.ItemCategory, java.util.List<com.teddybite.entity.types.Remark>> remarksMap = new java.util.HashMap<>();
        for (com.teddybite.entity.types.ItemCategory category : com.teddybite.entity.types.ItemCategory.values()) {
            com.teddybite.entity.remark.IRemark strategy = com.teddybite.entity.remark.RemarkFactory
                    .getStrategy(category);
            java.util.List<com.teddybite.entity.types.Remark> allowed = strategy.getAllowedRemarks();
            if (!allowed.isEmpty()) {
                remarksMap.put(category, allowed);
            }
        }
        return remarksMap;
    }
}
