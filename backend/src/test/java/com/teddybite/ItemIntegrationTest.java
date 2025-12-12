package com.teddybite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teddybite.entity.Item;
import com.teddybite.entity.types.ItemCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // disable security for tests
class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudFlow_forItem_works() throws Exception {
        // 1. Create with image
        Item createRequest = new Item(null, "Test Burger", 9.9, ItemCategory.BURGER, null);
        String createJson = objectMapper.writeValueAsString(createRequest);

        MockMultipartFile createPart = new MockMultipartFile(
                "item",
                "",
                "application/json",
                createJson.getBytes());

        MockMultipartFile createImagePart = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "dummy-image-content".getBytes());

        MvcResult createResult = mockMvc.perform(
                multipart("/api/items")
                        .file(createPart)
                        .file(createImagePart))
                .andExpect(status().isCreated())
                .andReturn();

        Item created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                Item.class);
        String id = created.getItemId();
        assertThat(id).isNotNull();

        // 2. Read
        mockMvc.perform(get("/api/items/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Test Burger"));

        // 3. Update with new image
        Item updateRequest = new Item(id, "Updated Burger", 10.5, ItemCategory.BURGER, null);
        String updateJson = objectMapper.writeValueAsString(updateRequest);

        MockMultipartFile updatePart = new MockMultipartFile(
                "item",
                "",
                "application/json",
                updateJson.getBytes());

        MockMultipartFile updateImagePart = new MockMultipartFile(
                "image",
                "updated-image.jpg",
                "image/jpeg",
                "updated-image-content".getBytes());

        mockMvc.perform(
                multipart("/api/items/" + id)
                        .file(updatePart)
                        .file(updateImagePart)
                        // multipart defaults to POST, force PUT
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Updated Burger"))
                .andExpect(jsonPath("$.itemPrice").value(10.5));

        // 4. Delete
        mockMvc.perform(delete("/api/items/" + id))
                .andExpect(status().isNoContent());

        // 5. Confirm deleted
        mockMvc.perform(get("/api/items/" + id))
                .andExpect(status().isNotFound());
    }
}
