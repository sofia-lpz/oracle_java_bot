package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemServiceImpl implements ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Override
    public ToDoItem createToDoItem(
            String title,
            String description,
            int storyPoints,
            float estimatedHours,
            float realHours
    ) {
        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setTitle(title);
        toDoItem.setDescription(description);
        toDoItem.setStoryPoints(storyPoints);
        toDoItem.setEstimatedHours(estimatedHours);
        toDoItem.setRealHours(realHours);
        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public List<ToDoItem> getAllToDoItems() {
        return toDoItemRepository.findAll();
    }

    @Override
    public ToDoItem getItemById(int id) {
        Optional<ToDoItem> optionalToDoItem = toDoItemRepository.findById(id);
        return optionalToDoItem.orElse(null);
    }

    @Override
    public ToDoItem addToDoItem(ToDoItem toDoItem) {
        return toDoItemRepository.save(toDoItem);
    }

    @Override
    public ToDoItem updateToDoItem(int id, ToDoItem toDoItem) {
        if (toDoItemRepository.existsById(id)) {
            toDoItem.setId(id);
            return toDoItemRepository.save(toDoItem);
        }
        return null;
    }

    @Override
    public Boolean deleteToDoItem(int id) {
        toDoItemRepository.deleteById(id);
        return !toDoItemRepository.existsById(id);
    }

    @Override
    public List<ToDoItem> getToDoItemsByProjectId(int projectId) {
        return toDoItemRepository.findByProjectId(projectId);
    }

    @Override
    public List<ToDoItem> getToDoItemsByUserId(int userId) {
        return toDoItemRepository.findByUserId(userId);
    }

    @Override
    public List<ToDoItem> getToDoItemsBySprintId(int sprintId) {
        return toDoItemRepository.findBySprintId(sprintId);
    }

    @Override
    public List<ToDoItem> getToDoItemsByStateId(int stateId) {
        return toDoItemRepository.findByStateId(stateId);
    }

    @Override
    public List<ToDoItem> findAll() {
        return toDoItemRepository.findAll();
    }
}