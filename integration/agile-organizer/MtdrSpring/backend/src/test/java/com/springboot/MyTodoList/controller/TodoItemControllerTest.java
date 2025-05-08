package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodoItemControllerTest {
    
    @Mock
    private ToDoItemService toDoItemService;
    
    @InjectMocks
    private ToDoItemController toDoItemController;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllToDoItems() {
        // Arrange
        ToDoItem item1 = new ToDoItem();
        item1.setID(1);
        item1.setTitle("Task 1");
        
        ToDoItem item2 = new ToDoItem();
        item2.setID(2);
        item2.setTitle("Task 2");
        
        List<ToDoItem> expectedItems = Arrays.asList(item1, item2);
        
        when(toDoItemService.findAll()).thenReturn(expectedItems);
        
        // Act
        List<ToDoItem> actualItems = toDoItemController.getAllToDoItems();
        
        // Assert
        assertEquals(expectedItems, actualItems);
        assertEquals(2, actualItems.size());
        verify(toDoItemService, times(1)).findAll();
    }
    
    @Test
    public void testGetToDoItemById_Success() {
        // Arrange
        int itemId = 1;
        ToDoItem expectedItem = new ToDoItem();
        expectedItem.setID(itemId);
        expectedItem.setTitle("Test Task");
        expectedItem.setDescription("Test Description");
        
        when(toDoItemService.getItemById(itemId)).thenReturn(expectedItem);
        
        // Act
        ResponseEntity<ToDoItem> response = toDoItemController.getToDoItemById(itemId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItem, response.getBody());
        verify(toDoItemService, times(1)).getItemById(itemId);
    }
    
    @Test
    public void testGetToDoItemById_NotFound() {
        // Arrange
        int itemId = 999;
        when(toDoItemService.getItemById(itemId)).thenReturn(null);
        
        // Act
        ResponseEntity<ToDoItem> response = toDoItemController.getToDoItemById(itemId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(toDoItemService, times(1)).getItemById(itemId);
    }
    
    @Test
    public void testGetToDoItemsByUserId_Success() {
        // Arrange
        int userId = 1;
        ToDoItem item1 = new ToDoItem();
        item1.setID(1);
        item1.setTitle("User Task 1");
        
        ToDoItem item2 = new ToDoItem();
        item2.setID(2);
        item2.setTitle("User Task 2");
        
        List<ToDoItem> expectedItems = Arrays.asList(item1, item2);
        
        when(toDoItemService.getToDoItemsByUserId(userId)).thenReturn(expectedItems);
        
        // Act
        ResponseEntity<List<ToDoItem>> response = toDoItemController.getToDoItemsByUserId(userId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(toDoItemService, times(1)).getToDoItemsByUserId(userId);
    }
    
    @Test
    public void testGetToDoItemsByUserId_NotFound() {
        // Arrange
        int userId = 999;
        when(toDoItemService.getToDoItemsByUserId(userId)).thenThrow(new RuntimeException("User not found"));
        
        // Act
        ResponseEntity<List<ToDoItem>> response = toDoItemController.getToDoItemsByUserId(userId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(toDoItemService, times(1)).getToDoItemsByUserId(userId);
    }
    
    @Test
    public void testAddToDoItem_Success() throws Exception {
        // Arrange
        State state = new State();
        state.setID(1);
        state.setName("To Do");
        
        User user = new User();
        user.setID(1);
        user.setName("Test User");
        
        ToDoItem newItem = new ToDoItem();
        newItem.setTitle("New Task");
        newItem.setDescription("New Description");
        newItem.setDueDate(OffsetDateTime.now().plusDays(7));
        newItem.setState(state);
        newItem.setUser(user);
        
        ToDoItem savedItem = new ToDoItem();
        savedItem.setID(3);
        savedItem.setTitle("New Task");
        savedItem.setDescription("New Description");
        savedItem.setDueDate(OffsetDateTime.now().plusDays(7));
        savedItem.setState(state);
        savedItem.setUser(user);
        
        when(toDoItemService.addToDoItem(newItem)).thenReturn(savedItem);
        
        // Act
        ResponseEntity<Void> response = toDoItemController.addToDoItem(newItem);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().get("location"));
        assertEquals("3", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
        verify(toDoItemService, times(1)).addToDoItem(newItem);
    }
    
    @Test
    public void testUpdateToDoItem_Success() {
        // Arrange
        int itemId = 1;
        ToDoItem itemToUpdate = new ToDoItem();
        itemToUpdate.setID(itemId);
        itemToUpdate.setTitle("Updated Title");
        itemToUpdate.setDescription("Updated Description");
        
        ToDoItem updatedItem = new ToDoItem();
        updatedItem.setID(itemId);
        updatedItem.setTitle("Updated Title");
        updatedItem.setDescription("Updated Description");
        
        when(toDoItemService.updateToDoItem(itemId, itemToUpdate)).thenReturn(updatedItem);
        
        // Act
        ResponseEntity<ToDoItem> response = toDoItemController.updateToDoItem(itemToUpdate, itemId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem, response.getBody());
        verify(toDoItemService, times(1)).updateToDoItem(itemId, itemToUpdate);
    }
    
    @Test
    public void testUpdateToDoItem_NotFound() {
        // Arrange
        int itemId = 999;
        ToDoItem itemToUpdate = new ToDoItem();
        itemToUpdate.setID(itemId);
        itemToUpdate.setTitle("Updated Title");
        
        when(toDoItemService.updateToDoItem(itemId, itemToUpdate)).thenReturn(null);
        
        // Act
        ResponseEntity<ToDoItem> response = toDoItemController.updateToDoItem(itemToUpdate, itemId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(toDoItemService, times(1)).updateToDoItem(itemId, itemToUpdate);
    }
    
    @Test
    public void testDeleteToDoItem_Success() {
        // Arrange
        int itemId = 1;
        when(toDoItemService.deleteToDoItem(itemId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = toDoItemController.deleteToDoItem(itemId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(toDoItemService, times(1)).deleteToDoItem(itemId);
    }
    
    @Test
    public void testDeleteToDoItem_NotFound() {
        // Arrange
        int itemId = 999;
        when(toDoItemService.deleteToDoItem(itemId)).thenReturn(false);
        
        // Act
        ResponseEntity<Boolean> response = toDoItemController.deleteToDoItem(itemId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody());
        verify(toDoItemService, times(1)).deleteToDoItem(itemId);
    }
    
    @Test
    public void testGetAllToDoItems_Empty() {
        // Arrange
        when(toDoItemService.findAll()).thenReturn(Collections.emptyList());
        
        // Act
        List<ToDoItem> result = toDoItemController.getAllToDoItems();
        
        // Assert
        assertEquals(0, result.size());
        verify(toDoItemService, times(1)).findAll();
    }
    
    @Test
    public void testAddToDoItem_ValidationFailure() throws Exception {
        // Arrange
        ToDoItem invalidItem = new ToDoItem();
        invalidItem.setTitle(""); // Empty title is invalid
        
        when(toDoItemService.addToDoItem(invalidItem))
            .thenThrow(new IllegalArgumentException("Title cannot be empty"));
        
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            toDoItemController.addToDoItem(invalidItem);
        });
        
        assertEquals("Title cannot be empty", exception.getMessage());
        verify(toDoItemService, times(1)).addToDoItem(invalidItem);
    }
}