package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/projects")
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping(value = "/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable int id) {
        try {
            Project project = projectService.getProjectById(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/projects")
    public ResponseEntity addProject(@RequestBody Project project) throws Exception {
        Project savedProject = projectService.addProject(project);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + savedProject.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    @PutMapping(value = "projects/{id}")
    public ResponseEntity updateProject(@RequestBody Project project, @PathVariable int id) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "projects/{id}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable("id") int id) {
        boolean flag = false;
        try {
            flag = projectService.deleteProject(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }
}