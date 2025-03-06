package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ToDoItemService {

    ToDoItem createToDoItem(
            String title,
            String description,
            int storyPoints,
            float estimatedHours,
            float realHours
    );

    List<ToDoItem> getAllToDoItems();

    ToDoItem getItemById(int id);

    ToDoItem addToDoItem(ToDoItem toDoItem);

    ToDoItem updateToDoItem(int id, ToDoItem toDoItem);

    Boolean deleteToDoItem(int id);

    List<ToDoItem> getToDoItemsByProjectId(int projectId);

    List<ToDoItem> getToDoItemsByUserId(int userId);

    List<ToDoItem> getToDoItemsBySprintId(int sprintId);

    List<ToDoItem> getToDoItemsByStateId(int stateId);

List<ToDoItem> findAll();


}
