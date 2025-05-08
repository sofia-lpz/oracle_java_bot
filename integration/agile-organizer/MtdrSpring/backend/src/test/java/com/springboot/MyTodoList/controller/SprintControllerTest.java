package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.service.SprintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SprintControllerTest {

    @Mock
    private SprintService sprintService;

    @InjectMocks
    private SprintController sprintController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSprints() {
        // Arrange
        Sprint sprint1 = new Sprint();
        sprint1.setID(1);
        sprint1.setName("Sprint 1");

        Sprint sprint2 = new Sprint();
        sprint2.setID(2);
        sprint2.setName("Sprint 2");

        List<Sprint> expectedSprints = Arrays.asList(sprint1, sprint2);

        when(sprintService.findAll()).thenReturn(expectedSprints);

        // Act
        List<Sprint> actualSprints = sprintController.getAllSprints();

        // Assert
        assertEquals(expectedSprints, actualSprints);
        assertEquals(2, actualSprints.size());
        verify(sprintService, times(1)).findAll();
    }

    @Test
    public void testGetSprintById_Success() {
        // Arrange
        int sprintId = 1;
        Sprint expectedSprint = new Sprint();
        expectedSprint.setID(sprintId);
        expectedSprint.setName("Sprint Alpha");
        
        when(sprintService.getSprintById(sprintId)).thenReturn(expectedSprint);
        
        // Act
        ResponseEntity<Sprint> response = sprintController.getSprintById(sprintId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSprint, response.getBody());
        verify(sprintService, times(1)).getSprintById(sprintId);
    }

    @Test
    public void testGetSprintById_NotFound() {
        // Arrange
        int sprintId = 99;
        when(sprintService.getSprintById(sprintId)).thenThrow(new RuntimeException("Sprint not found"));
        
        // Act
        ResponseEntity<Sprint> response = sprintController.getSprintById(sprintId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(sprintService, times(1)).getSprintById(sprintId);
    }

    @Test
    public void testAddSprint_Success() throws Exception {
        // Arrange
        Project project = new Project();
        project.setID(1);
        project.setName("Test Project");

        State state = new State();
        state.setID(1);
        state.setName("Active");

        Sprint newSprint = new Sprint();
        newSprint.setName("New Sprint");
        newSprint.setDueDate(OffsetDateTime.now().plusDays(14));
        newSprint.setProject(project);
        newSprint.setState(state);
        
        Sprint savedSprint = new Sprint();
        savedSprint.setID(3);
        savedSprint.setName("New Sprint");
        savedSprint.setDueDate(OffsetDateTime.now().plusDays(14));
        savedSprint.setProject(project);
        savedSprint.setState(state);
        
        when(sprintService.addSprint(newSprint)).thenReturn(savedSprint);
        
        // Act
        ResponseEntity response = sprintController.addSprint(newSprint);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sprintService, times(1)).addSprint(newSprint);
        assertEquals("3", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
    }

    @Test
    public void testUpdateSprint_Success() {
        // Arrange
        int sprintId = 1;
        Sprint sprintToUpdate = new Sprint();
        sprintToUpdate.setID(sprintId);
        sprintToUpdate.setName("Updated Sprint");
        
        Sprint updatedSprint = new Sprint();
        updatedSprint.setID(sprintId);
        updatedSprint.setName("Updated Sprint");
        
        when(sprintService.updateSprint(sprintId, sprintToUpdate)).thenReturn(updatedSprint);
        
        // Act
        ResponseEntity response = sprintController.updateSprint(sprintToUpdate, sprintId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSprint, response.getBody());
        verify(sprintService, times(1)).updateSprint(sprintId, sprintToUpdate);
    }

    @Test
    public void testUpdateSprint_NotFound() {
        // Arrange
        int sprintId = 99;
        Sprint sprintToUpdate = new Sprint();
        sprintToUpdate.setID(sprintId);
        sprintToUpdate.setName("Updated Sprint");
        
        when(sprintService.updateSprint(sprintId, sprintToUpdate)).thenReturn(null);
        
        // Act
        ResponseEntity response = sprintController.updateSprint(sprintToUpdate, sprintId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(sprintService, times(1)).updateSprint(sprintId, sprintToUpdate);
    }

    @Test
    public void testDeleteSprint_Success() {
        // Arrange
        int sprintId = 1;
        when(sprintService.deleteSprint(sprintId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = sprintController.deleteSprint(sprintId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(sprintService, times(1)).deleteSprint(sprintId);
    }

    @Test
    public void testDeleteSprint_NotFound() {
        // Arrange
        int sprintId = 99;
        when(sprintService.deleteSprint(sprintId)).thenReturn(false);
        
        // Act
        ResponseEntity<Boolean> response = sprintController.deleteSprint(sprintId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(sprintService, times(1)).deleteSprint(sprintId);
    }
}