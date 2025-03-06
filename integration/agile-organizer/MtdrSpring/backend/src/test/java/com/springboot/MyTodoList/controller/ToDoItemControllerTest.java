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

import java.util.Arrays;
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
}