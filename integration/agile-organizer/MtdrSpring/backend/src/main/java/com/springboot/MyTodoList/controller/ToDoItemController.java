package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Kpi;
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

    // @CrossOrigin
    @GetMapping(value = "/todolist")
    public List<ToDoItem> getAllToDoItems() {
        return toDoItemService.findAll();
    }

    @GetMapping(value = "/todolist/usersummary/{userId}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsByUserId(@PathVariable int userId) {
        try {
            List<ToDoItem> toDoItems = toDoItemService.getToDoItemsByUserId(userId);
            return new ResponseEntity<>(toDoItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/todolist/summary")
    public ResponseEntity<List<ToDoItem>> getToDoItemsSummary(
            @RequestParam(required = false) List<Integer> userId,
            @RequestParam(required = false) List<Integer> teamId,
            @RequestParam(required = false) List<Integer> projectId,
            @RequestParam(required = false) List<Integer> sprintId,
            @RequestParam(required = false) Boolean done) {
        try {
            List<ToDoItem> toDoItems = toDoItemService.getToDoItemsSummary(userId, teamId, projectId, sprintId, done);
            return new ResponseEntity<>(toDoItems, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @CrossOrigin
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
        try {
            ToDoItem toDoItem = toDoItemService.getItemById(id);
            if (toDoItem == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(toDoItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @CrossOrigin
    @PostMapping(value = "/todolist")
    public ResponseEntity<Void> addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
        ToDoItem td = toDoItemService.addToDoItem(todoItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        // URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    // @CrossOrigin
    @PutMapping(value = "todolist/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
        try {
            ToDoItem updatedToDoItem = toDoItemService.updateToDoItem(id, toDoItem);
            if (updatedToDoItem == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedToDoItem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @CrossOrigin
    @DeleteMapping(value = "todolist/{id}")
    public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
        Boolean flag = false;
        try {
            flag = toDoItemService.deleteToDoItem(id);
            if (!flag) {
                return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
