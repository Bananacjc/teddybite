package com.teddybite.service;

import com.teddybite.entity.Item;
import com.teddybite.entity.types.ItemCategory;
import com.teddybite.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private MultipartFile multipartFile;

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
    void createItem_withLegacyUrl_cleansUrl() {
        Item inputItem = new Item();
        inputItem.setItemName("Chicken Burger");
        inputItem.setItemImage("http://localhost:8080/uploads/image.png");

        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

        Item result = itemService.createItem(inputItem, null);

        assertEquals("image.png", result.getItemImage());
    }

    @Test
    void createItem_withNewImage_savesImageSuccessfully() throws IOException {
        // Mock static Files and Paths to prevent real file creation
        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

            // Setup Mocks
            Path mockPath = mock(Path.class);
            pathsMock.when(() -> Paths.get("uploads")).thenReturn(mockPath);
            when(mockPath.resolve(anyString())).thenReturn(mockPath);

            // Files.exists returns false -> triggers createDirectories
            filesMock.when(() -> Files.exists(mockPath)).thenReturn(false);

            // Setup MultipartFile
            when(multipartFile.isEmpty()).thenReturn(false);
            when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
            when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("test data".getBytes()));

            Item inputItem = new Item();
            when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

            // Execute
            Item result = itemService.createItem(inputItem, multipartFile);

            // Verify
            assertTrue(result.getItemImage().endsWith("_test.jpg")); // UUID prefix + filename

            // Verify static calls
            filesMock.verify(() -> Files.createDirectories(mockPath));
            filesMock.verify(() -> Files.copy(any(ByteArrayInputStream.class), eq(mockPath), any()));
        }
    }

    @Test
    void createItem_saveImageIOException_throwsRuntimeException() throws IOException {
        // Scenario: File system throws IOException during copy
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getInputStream()).thenThrow(new IOException("Disk error"));

        Item inputItem = new Item();

        // Execute & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> itemService.createItem(inputItem, multipartFile));
        assertEquals("Failed to store file", ex.getMessage());
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
    void updateItem_withLegacyUrl_cleansUrl() {
        Item inputItem = new Item();
        inputItem.setItemId("I001");
        inputItem.setItemImage("http://localhost:8080/uploads/image.png");

        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

        Item result = itemService.updateItem(inputItem, null);

        assertEquals("image.png", result.getItemImage());
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

    @Test
    void deleteItemById_withImage_deletesFile() {
        // Scenario: Deleting an item should delete its associated image file
        Item existingItem = new Item();
        existingItem.setItemId("I001");
        existingItem.setItemImage("image.png");

        when(itemRepository.findById("I001")).thenReturn(Optional.of(existingItem));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

            Path mockPath = mock(Path.class);
            pathsMock.when(() -> Paths.get("uploads")).thenReturn(mockPath);
            when(mockPath.resolve("image.png")).thenReturn(mockPath);

            // Execute
            itemService.deleteItemById("I001");

            // Verify Files.deleteIfExists was called
            filesMock.verify(() -> Files.deleteIfExists(mockPath), times(1));
            verify(itemRepository).deleteById("I001");
        }
    }

    @Test
    void deleteItemById_imageWithUploadsPath_parsesAndDeletes() {
        // Scenario: Image URL contains "/uploads/", logic should extract filename
        Item existingItem = new Item();
        existingItem.setItemId("I001");
        existingItem.setItemImage("http://url/uploads/image.png");

        when(itemRepository.findById("I001")).thenReturn(Optional.of(existingItem));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

            Path mockPath = mock(Path.class);
            pathsMock.when(() -> Paths.get("uploads")).thenReturn(mockPath);
            // Verify it resolves the EXTRACTED filename, not the full URL
            when(mockPath.resolve("image.png")).thenReturn(mockPath);

            itemService.deleteItemById("I001");

            filesMock.verify(() -> Files.deleteIfExists(mockPath));
        }
    }

    @Test
    void deleteItemById_ioException_logsErrorButDoesNotThrow() {
        // Scenario: Files.deleteIfExists throws IOException.
        // Logic should catch it, print to stderr, and NOT crash.
        Item existingItem = new Item();
        existingItem.setItemId("I001");
        existingItem.setItemImage("image.png");

        when(itemRepository.findById("I001")).thenReturn(Optional.of(existingItem));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
                MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {

            Path mockPath = mock(Path.class);
            pathsMock.when(() -> Paths.get("uploads")).thenReturn(mockPath);
            when(mockPath.resolve("image.png")).thenReturn(mockPath);

            // Force IOException
            filesMock.when(() -> Files.deleteIfExists(mockPath)).thenThrow(new IOException("Locked"));

            // Execute - Should NOT throw exception
            assertDoesNotThrow(() -> itemService.deleteItemById("I001"));

            verify(itemRepository).deleteById("I001");
        }
    }

    @Test
    void updateItem_withNewImage_deletesOldAndSavesNew() throws IOException {
        // Scenario: Updating an item that already has "old_image.png" with a NEW file
        // "new.jpg"

        // 1. Setup Input Data
        Item inputItem = new Item();
        inputItem.setItemId("I001");
        inputItem.setItemName("Burger");
        // The input item might not have the old image set on it,
        // the service should fetch it from DB to find the old string.

        // 2. Setup Existing DB Data (The "Old" state)
        Item existingDbItem = new Item();
        existingDbItem.setItemId("I001");
        existingDbItem.setItemImage("old_image.png");

        when(itemRepository.findById("I001")).thenReturn(Optional.of(existingDbItem));
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

        // 3. Mock Multipart File (The "New" image)
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("new.jpg");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        
        // 4. Mock Static Files/Paths
        try (MockedStatic<Files> filesMock = mockStatic(Files.class);
             MockedStatic<Paths> pathsMock = mockStatic(Paths.class)) {
            
            Path uploadsDir = mock(Path.class);
            Path oldImagePath = mock(Path.class);
            
            pathsMock.when(() -> Paths.get("uploads")).thenReturn(uploadsDir);
            
            // --- FIX START ---
            // Move the generic stub UP. This handles the UUID image name for saving.
            when(uploadsDir.resolve(anyString())).thenReturn(uploadsDir); 
            
            // Move the specific stub DOWN. This ensures "old_image.png" returns 'oldImagePath'
            when(uploadsDir.resolve("old_image.png")).thenReturn(oldImagePath);
            // --- FIX END ---
            
            // 5. Execute
            Item result = itemService.updateItem(inputItem, multipartFile);

            // 6. Verify
            filesMock.verify(() -> Files.deleteIfExists(oldImagePath), times(1));
            
            assertTrue(result.getItemImage().endsWith("_new.jpg"));

            filesMock.verify(() -> Files.copy(
                any(java.io.InputStream.class), 
                eq(uploadsDir), 
                any(java.nio.file.CopyOption.class)
            ));
        }
    }
}
