package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    public List<ToDoItem> findAll(){
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }

    public ToDoItem getItemById(int id) {
        return toDoItemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ToDoItem not found with id: " + id));
    }

    public List<ToDoItem> getToDoItemsByUserId(int userId) {
        return toDoItemRepository.getToDoItemsByUserID(userId);
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem){
        return toDoItemRepository.save(toDoItem);
    }

    public boolean deleteToDoItem(int id){
        try{
            toDoItemRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public ToDoItem updateToDoItem(int id, ToDoItem td){
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if(toDoItemData.isPresent()){
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setID(id);
            toDoItem.setTitle(td.getTitle());
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDueDate(td.getDueDate());
            toDoItem.setState(td.getState());
            toDoItem.setSprint(td.getSprint());
            toDoItem.setUser(td.getUser());
            toDoItem.setProject(td.getProject());
            toDoItem.setStoryPoints(td.getStoryPoints());
            toDoItem.setPriority(td.getPriority());
            toDoItem.setDeleted(td.getDeleted());
            toDoItem.setDone(td.isDone());
            toDoItem.setEstimatedHours(td.getEstimatedHours());
            toDoItem.setRealHours(td.getRealHours());
            return toDoItemRepository.save(toDoItem);
        }else{
            return null;
        }
    }

}
