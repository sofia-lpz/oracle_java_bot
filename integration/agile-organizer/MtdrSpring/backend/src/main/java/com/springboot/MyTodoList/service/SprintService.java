package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.repository.SprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Sprint getLatestSprint() {
        List<Sprint> sprints = sprintRepository.findAll();
        if (sprints.isEmpty()) {
            return null;
        }
        return sprints.get(sprints.size() - 1);
    }

    public Sprint getSprintById(int id) {
        return sprintRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));
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