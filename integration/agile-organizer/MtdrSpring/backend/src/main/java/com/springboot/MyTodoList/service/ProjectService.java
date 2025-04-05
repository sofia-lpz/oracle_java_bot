package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findAll() {
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

    public ResponseEntity<Project> getProjectById(int id) {
        Optional<Project> projectData = projectRepository.findById(id);
        if (projectData.isPresent()) {
            return new ResponseEntity<>(projectData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public boolean deleteProject(int id) {
        try {
            projectRepository.deleteById(id);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public Project updateProject(int id, Project project) {
        Optional<Project> projectData = projectRepository.findById(id);
        if(projectData.isPresent()) {
            Project existingProject = projectData.get();
            existingProject.setID(id);
            existingProject.setName(project.getName());
            existingProject.setDescription(project.getDescription());
            existingProject.setEndDate(project.getEndDate());
            return projectRepository.save(existingProject);
        } else {
            return null;
        }
    }
}