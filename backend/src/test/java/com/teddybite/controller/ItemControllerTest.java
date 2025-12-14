package com.teddybite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.entity.Item;
import com.teddybite.entity.types.ItemCategory;
import com.teddybite.service.interfaceService.IItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // run with real Spring app, but no security filters
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IItemService itemService; // mocked – no real DB / file operations

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void save_validItemMultipart_returnsCreated() throws Exception {
        Item saved = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null);
        when(itemService.createItem(any(Item.class), any())).thenReturn(saved);

        String itemJson = objectMapper.writeValueAsString(
                new Item(null, "Burger", 9.9, ItemCategory.BURGER, null));

        MockMultipartFile itemPart = new MockMultipartFile(
                "item",
                "",
                "application/json",
                itemJson.getBytes());

        mockMvc.perform(multipart("/api/items").file(itemPart))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.itemId").value("I001"))
                .andExpect(jsonPath("$.itemName").value("Burger"));
    }

    @Test
    void findAll_returnsOkWithList() throws Exception {
        List<Item> items = List.of(
                new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null));
        when(itemService.readItemAll()).thenReturn(items);

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value("I001"))
                .andExpect(jsonPath("$[0].itemName").value("Burger"));
    }

    @Test
    void findById_existing_returnsOk() throws Exception {
        Item item = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null);
        when(itemService.readItemById("I001")).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/items/I001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Burger"));
    }

    @Test
    void findById_missing_returnsNotFound() throws Exception {
        when(itemService.readItemById("I999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/items/I999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById_existing_returnsNoContent() throws Exception {
        Item existing = new Item("I001", "Burger", 9.9, ItemCategory.BURGER, null);

        when(itemService.readItemById("I001")).thenReturn(Optional.of(existing));

        mockMvc.perform(delete("/api/items/I001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_missing_returnsNotFound() throws Exception {
        when(itemService.readItemById("I999")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/items/I999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCategories_returnsOkAndNonEmpty() throws Exception {
        mockMvc.perform(get("/api/items/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists()); // at least one category
    }

    @Test
    void getRemarks_returnsOk() throws Exception {
        mockMvc.perform(get("/api/items/remarks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

}
