package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
public class ToDoItemController {

    @Autowired
    private ToDoItemService toDoItemService;

    @GetMapping("/todo")
    public List<ToDoItem> getAllToDoItems() {
        return toDoItemService.getAllToDoItems();
    }

    @PostMapping("/todo")
    public ResponseEntity<ToDoItem> addToDoItem(@RequestBody ToDoItem toDoItem) {
        ToDoItem newToDoItem = toDoItemService.addToDoItem(toDoItem);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/todo/" + newToDoItem.getId()));
        return new ResponseEntity<>(newToDoItem, headers, HttpStatus.CREATED);
    }
}
