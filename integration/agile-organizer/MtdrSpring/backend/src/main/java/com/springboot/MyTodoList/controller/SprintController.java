package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.service.SprintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SprintController {
    @Autowired
    private SprintService sprintService;

    @GetMapping(value = "/sprints")
    public List<Sprint> getAllSprints() {
        return sprintService.findAll();
    }

    @GetMapping(value = "/sprints/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable int id) {
        try {
            ResponseEntity<Sprint> responseEntity = sprintService.getSprintById(id);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/sprints")
    public ResponseEntity addSprint(@RequestBody Sprint sprint) throws Exception {
        Sprint savedSprint = sprintService.addSprint(sprint);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + savedSprint.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    @PutMapping(value = "sprints/{id}")
    public ResponseEntity updateSprint(@RequestBody Sprint sprint, @PathVariable int id) {
        try {
            Sprint updatedSprint = sprintService.updateSprint(id, sprint);
            return new ResponseEntity<>(updatedSprint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "sprints/{id}")
    public ResponseEntity<Boolean> deleteSprint(@PathVariable("id") int id) {
        boolean flag = false;
        try {
            flag = sprintService.deleteSprint(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }
}