package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Sprint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SprintService {

    public List<Sprint> findAll();

    public Sprint getLatestSprint();

    public Sprint getSprintById(int id);

    public Sprint addSprint(Sprint sprint);
    public boolean deleteSprint(int id);
    
    public Sprint updateSprint(int id, Sprint sprint);
}