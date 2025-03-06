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
public class ToDoItemServiceImpl implements ToDoItemService{

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
    public Optional<ToDoItem> getToDoItemById(Long id) {
        return toDoItemRepository.findById(id);
    }

    @Override
    public ResponseEntity<ToDoItem> updateToDoItem(
            Long id,
            String title,
            String description,
            int storyPoints,
            float estimatedHours,
            float realHours
    ) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);

        if (toDoItemData.isPresent()) {
            ToDoItem _toDoItem = toDoItemData.get();
            _toDoItem.setTitle(title);
            _toDoItem.setDescription(description);
            _toDoItem.setStoryPoints(storyPoints);
            _toDoItem.setEstimatedHours(estimatedHours);
            _toDoItem.setRealHours(realHours);
            return new ResponseEntity<>(toDoItemRepository.save(_toDoItem), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteToDoItem(Long id) {
        try {
            toDoItemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
