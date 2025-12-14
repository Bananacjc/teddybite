package com.teddybite.service;

import com.teddybite.entity.Item;
import com.teddybite.entity.types.ItemCategory;
import com.teddybite.repository.ItemRepository;
import com.teddybite.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_withoutImage_usesProvidedFieldsAndSaves() {
        Item item = new Item(null, "Chicken Burger", 9.9, ItemCategory.BURGER, "http://image/url");
        // let repository return whatever was passed in
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Item result = itemService.createItem(item, null);

        assertNotNull(result);
        assertEquals("Chicken Burger", result.getItemName());
        assertEquals(9.9, result.getItemPrice());
        assertEquals(ItemCategory.BURGER, result.getItemCategory());
        assertEquals("http://image/url", result.getItemImage());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void readItemAll_returnsListFromRepository() {
        List<Item> items = Arrays.asList(
                new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null),
                new Item("I002", "Coke", 3.0, ItemCategory.BEVERAGES, null));
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.readItemAll();

        assertEquals(2, result.size());
        assertEquals("I001", result.get(0).getItemId());
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void readItemById_existingId_returnsOptionalItem() {
        Item item = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null);
        when(itemRepository.findById("I001")).thenReturn(Optional.of(item));

        Optional<Item> result = itemService.readItemById("I001");

        assertTrue(result.isPresent());
        assertEquals("Burger", result.get().getItemName());
        verify(itemRepository, times(1)).findById("I001");
    }

    @Test
    void readItemById_missingId_returnsEmptyOptional() {
        when(itemRepository.findById("I999")).thenReturn(Optional.empty());

        Optional<Item> result = itemService.readItemById("I999");

        assertTrue(result.isEmpty());
        verify(itemRepository, times(1)).findById("I999");
    }

    @Test
    void updateItem_withoutNewImage_savesItem() {
        Item item = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, "oldUrl");
        when(itemRepository.save(item)).thenReturn(item);

        Item result = itemService.updateItem(item, null);

        assertEquals("I001", result.getItemId());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void deleteItemById_existingItem_deletesFromRepository() {
        Item item = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null);
        when(itemRepository.findById("I001")).thenReturn(Optional.of(item));

        itemService.deleteItemById("I001");

        verify(itemRepository, times(1)).findById("I001");
        verify(itemRepository, times(1)).deleteById("I001");
    }

    @Test
    void deleteItemById_missingItem_doesNothing() {
        when(itemRepository.findById("I999")).thenReturn(Optional.empty());

        itemService.deleteItemById("I999");

        verify(itemRepository, times(1)).findById("I999");
        verify(itemRepository, never()).deleteById(anyString());
    }
}
