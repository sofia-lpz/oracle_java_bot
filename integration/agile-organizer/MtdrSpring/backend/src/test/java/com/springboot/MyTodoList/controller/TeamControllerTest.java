package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class TeamControllerTest {
    
    @Mock
    private TeamService teamService;
    
    @InjectMocks
    private TeamController teamController;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllTeams() {
        // Arrange
        Team team1 = new Team();
        team1.setID(1);
        team1.setTeamName("Development Team");
        
        Team team2 = new Team();
        team2.setID(2);
        team2.setTeamName("QA Team");
        
        List<Team> expectedTeams = Arrays.asList(team1, team2);
        
        when(teamService.findAll()).thenReturn(expectedTeams);
        
        // Act
        List<Team> actualTeams = teamController.getAllTeams();
        
        // Assert
        assertEquals(expectedTeams, actualTeams);
        verify(teamService, times(1)).findAll();
    }
    
    @Test
    public void testGetTeamById_Success() {
        // Arrange
        int teamId = 1;
        Team expectedTeam = new Team();
        expectedTeam.setID(teamId);
        expectedTeam.setTeamName("Development Team");
        
        when(teamService.getTeamById(teamId)).thenReturn(expectedTeam);
        
        // Act
        ResponseEntity<Team> response = teamController.getTeamById(teamId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTeam, response.getBody());
        verify(teamService, times(1)).getTeamById(teamId);
    }
    
    @Test
    public void testGetTeamById_NotFound() {
        // Arrange
        int teamId = 99;
        when(teamService.getTeamById(teamId)).thenThrow(new RuntimeException("Team not found"));
        
        // Act
        ResponseEntity<Team> response = teamController.getTeamById(teamId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(teamService, times(1)).getTeamById(teamId);
    }
    
    @Test
    public void testAddTeam_Success() throws Exception {
        // Arrange
        Team newTeam = new Team();
        newTeam.setTeamName("New Team");
        
        Team savedTeam = new Team();
        savedTeam.setID(3);
        savedTeam.setTeamName("New Team");
        
        when(teamService.addTeam(newTeam)).thenReturn(savedTeam);
        
        // Act
        ResponseEntity response = teamController.addTeam(newTeam);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(teamService, times(1)).addTeam(newTeam);
        assertEquals("3", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
    }
    
    @Test
    public void testUpdateTeam_Success() {
        // Arrange
        int teamId = 1;
        Team teamToUpdate = new Team();
        teamToUpdate.setID(teamId);
        teamToUpdate.setTeamName("Updated Team");
        
        Team updatedTeam = new Team();
        updatedTeam.setID(teamId);
        updatedTeam.setTeamName("Updated Team");
        
        when(teamService.updateTeam(teamId, teamToUpdate)).thenReturn(updatedTeam);
        
        // Act
        ResponseEntity response = teamController.updateTeam(teamToUpdate, teamId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTeam, response.getBody());
        verify(teamService, times(1)).updateTeam(teamId, teamToUpdate);
    }
    
    @Test
    public void testUpdateTeam_NotFound() {
        // Arrange
        int teamId = 99;
        Team teamToUpdate = new Team();
        teamToUpdate.setID(teamId);
        teamToUpdate.setTeamName("Updated Team");
        
        when(teamService.updateTeam(teamId, teamToUpdate)).thenThrow(new RuntimeException("Team not found"));
        
        // Act
        ResponseEntity response = teamController.updateTeam(teamToUpdate, teamId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(teamService, times(1)).updateTeam(teamId, teamToUpdate);
    }
    
    @Test
    public void testDeleteTeam_Success() {
        // Arrange
        int teamId = 1;
        when(teamService.deleteTeam(teamId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = teamController.deleteTeam(teamId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(teamService, times(1)).deleteTeam(teamId);
    }
    
    @Test
    public void testDeleteTeam_NotFound() {
        // Arrange
        int teamId = 99;
        when(teamService.deleteTeam(teamId)).thenThrow(new RuntimeException("Team not found"));
        
        // Act
        ResponseEntity<Boolean> response = teamController.deleteTeam(teamId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(teamService, times(1)).deleteTeam(teamId);
    }
}