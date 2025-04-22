package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> findAll() {
        List<Team> teams = teamRepository.findAll();
        return teams;
    }

    public Team getTeamById(int id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
    }

    public Team addTeam(Team team) {
        return teamRepository.save(team);
    }

    public boolean deleteTeam(int id) {
        try {
            teamRepository.deleteById(id);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public Team updateTeam(int id, Team team) {
        Optional<Team> teamData = teamRepository.findById(id);
        if(teamData.isPresent()) {
            Team existingTeam = teamData.get();
            existingTeam.setID(id);
            existingTeam.setTeamName(team.getTeamName());
            existingTeam.setTeamDescription(team.getTeamDescription());
            return teamRepository.save(existingTeam);
        } else {
            return null;
        }
    }
}