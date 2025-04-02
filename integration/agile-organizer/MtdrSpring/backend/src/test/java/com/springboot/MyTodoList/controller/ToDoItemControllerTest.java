package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ToDoItemControllerTest {

    @Mock
    private ToDoItemService toDoItemService;

    @InjectMocks
    private ToDoItemController toDoItemController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllToDoItems() {
        ToDoItem item1 = new ToDoItem();
        ToDoItem item2 = new ToDoItem();
        List<ToDoItem> itemList = Arrays.asList(item1, item2);

        when(toDoItemService.findAll()).thenReturn(itemList);

        List<ToDoItem> result = toDoItemController.getAllToDoItems();
        assertEquals(2, result.size());
        verify(toDoItemService, times(1)).findAll();
    }

    @Test
    public void testGetToDoItemById() {
        ToDoItem item = new ToDoItem();
        when(toDoItemService.getItemById(1)).thenReturn(new ResponseEntity<>(item, HttpStatus.OK));

        ResponseEntity<ToDoItem> result = toDoItemController.getToDoItemById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).getItemById(1);
    }

    @Test
    public void testAddToDoItem() throws Exception {
        ToDoItem item = new ToDoItem();
        when(toDoItemService.addToDoItem(item)).thenReturn(item);

        ResponseEntity result = toDoItemController.addToDoItem(item);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testUpdateToDoItem() {
        ToDoItem item = new ToDoItem();
        when(toDoItemService.updateToDoItem(1, item)).thenReturn(item);

        ResponseEntity result = toDoItemController.updateToDoItem(item, 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).updateToDoItem(1, item);
    }

    @Test
    public void testDeleteToDoItem() {
        when(toDoItemService.deleteToDoItem(1)).thenReturn(true);

        ResponseEntity<Boolean> result = toDoItemController.deleteToDoItem(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).deleteToDoItem(1);
    }

    @Test
    public void testSuccessfulToDoItemCreation() throws Exception {
        // Prepare valid ToDoItem
        ToDoItem item = new ToDoItem();
        item.setTitle("Test Task");
        item.setDescription("Test Description");

        when(toDoItemService.addToDoItem(item)).thenReturn(item);

        ResponseEntity result = toDoItemController.addToDoItem(item);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testToDoItemCreationWithEmptyFields() throws Exception {
        // Create a ToDoItem with empty title
        ToDoItem item = new ToDoItem();
        item.setTitle("");
        item.setDescription("Test Description");

        // Simulate validation error in service
        when(toDoItemService.addToDoItem(item)).thenThrow(new IllegalArgumentException("Title cannot be empty"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.addToDoItem(item);
        });

        assertEquals("Title cannot be empty", exception.getMessage());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testToDoItemCreationWithNegativeHours() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setTitle("Test Task");
        item.setEstimatedHours(-5);

        when(toDoItemService.addToDoItem(item))
                .thenThrow(new IllegalArgumentException("Estimated hours cannot be negative"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.addToDoItem(item);
        });

        assertEquals("Estimated hours cannot be negative", exception.getMessage());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testToDoItemCreationWithDuplicateTitle() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setTitle("Existing Title");

        when(toDoItemService.addToDoItem(item))
                .thenThrow(new IllegalArgumentException("Task with this title already exists"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.addToDoItem(item);
        });

        assertEquals("Task with this title already exists", exception.getMessage());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testGetToDoItemByIdNotFound() {
        when(toDoItemService.getItemById(999)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<ToDoItem> result = toDoItemController.getToDoItemById(999);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(toDoItemService, times(1)).getItemById(999);
    }

    @Test
    public void testGetAllToDoItemsEmpty() {
        when(toDoItemService.findAll()).thenReturn(Collections.emptyList());

        List<ToDoItem> result = toDoItemController.getAllToDoItems();
        assertEquals(0, result.size());
        verify(toDoItemService, times(1)).findAll();
    }

    // ...existing code...

    @Test
    public void testUpdateToDoItemNotFound() {
        ToDoItem item = new ToDoItem();
        item.setTitle("Updated Title");

        // Simulate not found in service
        when(toDoItemService.updateToDoItem(999, item)).thenReturn(null);

        ResponseEntity result = toDoItemController.updateToDoItem(item, 999);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(toDoItemService, times(1)).updateToDoItem(999, item);
    }

    @Test
    public void testUpdateToDoItemWithInvalidData() {
        ToDoItem item = new ToDoItem();
        item.setTitle(""); // Empty title is invalid

        // Simulate validation error in service
        when(toDoItemService.updateToDoItem(1, item)).thenThrow(new IllegalArgumentException("Title cannot be empty"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.updateToDoItem(item, 1);
        });

        assertEquals("Title cannot be empty", exception.getMessage());
        verify(toDoItemService, times(1)).updateToDoItem(1, item);
    }

    @Test
    public void testUpdateToDoItemWithNegativeRealHours() {
        ToDoItem item = new ToDoItem();
        item.setTitle("Test Task");
        item.setRealHours(-10);

        when(toDoItemService.updateToDoItem(1, item))
                .thenThrow(new IllegalArgumentException("Real hours cannot be negative"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.updateToDoItem(item, 1);
        });

        assertEquals("Real hours cannot be negative", exception.getMessage());
        verify(toDoItemService, times(1)).updateToDoItem(1, item);
    }

    @Test
    public void testDeleteToDoItemNotFound() {
        when(toDoItemService.deleteToDoItem(999)).thenReturn(false);

        ResponseEntity<Boolean> result = toDoItemController.deleteToDoItem(999);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(false, result.getBody());
        verify(toDoItemService, times(1)).deleteToDoItem(999);
    }

    @Test
    public void testDeleteToDoItemUnauthorized() {
        when(toDoItemService.deleteToDoItem(1))
                .thenThrow(new AccessDeniedException("User not authorized to delete this item"));

        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            toDoItemController.deleteToDoItem(1);
        });

        assertEquals("User not authorized to delete this item", exception.getMessage());
        verify(toDoItemService, times(1)).deleteToDoItem(1);
    }

    // ...existing code...

    @Test
    public void testCreateTaskWithNonExistentState() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setTitle("Test Task");
        State nonExistentState = new State();
        nonExistentState.setID(999);
        item.setState(nonExistentState);

        // Simulate validation error in service
        when(toDoItemService.addToDoItem(item)).thenThrow(new IllegalArgumentException("State does not exist"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.addToDoItem(item);
        });

        assertEquals("State does not exist", exception.getMessage());
        verify(toDoItemService, times(1)).addToDoItem(item);
    }

    @Test
    public void testUpdateTaskWithNegativeStoryPoints() {
        ToDoItem item = new ToDoItem();
        item.setTitle("Test Task");
        item.setStoryPoints(-3);

        when(toDoItemService.updateToDoItem(1, item))
                .thenThrow(new IllegalArgumentException("Story points cannot be negative"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.updateToDoItem(item, 1);
        });

        assertEquals("Story points cannot be negative", exception.getMessage());
        verify(toDoItemService, times(1)).updateToDoItem(1, item);
    }

}