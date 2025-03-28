package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping(value = "/teams")
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @GetMapping(value = "/teams/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable int id) {
        try {
            ResponseEntity<Team> responseEntity = teamService.getTeamById(id);
            return new ResponseEntity<Team>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/teams")
    public ResponseEntity addTeam(@RequestBody Team team) throws Exception {
        Team savedTeam = teamService.addTeam(team);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + savedTeam.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    @PutMapping(value = "teams/{id}")
    public ResponseEntity updateTeam(@RequestBody Team team, @PathVariable int id) {
        try {
            Team updatedTeam = teamService.updateTeam(id, team);
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "teams/{id}") 
    public ResponseEntity<Boolean> deleteTeam(@PathVariable("id") int id) {
        Boolean flag = false;
        try {
            flag = teamService.deleteTeam(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }
}