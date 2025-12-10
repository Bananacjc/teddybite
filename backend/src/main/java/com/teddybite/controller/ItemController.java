package com.teddybite.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<Item> save(@Valid @RequestBody Item item) {
        Item savedItem = itemService.createItem(item);

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

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable String id,
        @Valid @RequestBody Item updateItem) {

            return itemService.readItemById(id)
                    .map(currentItem -> {
                        currentItem.setItemName(updateItem.getItemName());
                        currentItem.setItemCategory(updateItem.getItemCategory());
                        currentItem.setItemPrice(updateItem.getItemPrice());
                        
                        Item updatedItem = itemService.updateItem(currentItem);
                        
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

}
