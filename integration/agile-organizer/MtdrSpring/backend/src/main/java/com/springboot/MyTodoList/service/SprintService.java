package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    public List<Sprint> findAll() {
        List<Sprint> sprints = sprintRepository.findAll();
        return sprints;
    }

    public ResponseEntity<Sprint> getSprintById(int id) {
        Optional<Sprint> sprintData = sprintRepository.findById(id);
        if (sprintData.isPresent()) {
            return new ResponseEntity<>(sprintData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Sprint addSprint(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    public boolean deleteSprint(int id) {
        try {
            sprintRepository.deleteById(id);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public Sprint updateSprint(int id, Sprint sprint) {
        Optional<Sprint> sprintData = sprintRepository.findById(id);
        if(sprintData.isPresent()) {
            Sprint existingSprint = sprintData.get();
            existingSprint.setID(id);
            existingSprint.setName(sprint.getName());
            existingSprint.setDueDate(sprint.getDueDate());
            existingSprint.setState(sprint.getState());
            existingSprint.setProject(sprint.getProject());
            return sprintRepository.save(existingSprint);
        } else {
            return null;
        }
    }
}