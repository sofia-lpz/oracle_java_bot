package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;
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

public class ProjectControllerTest {
    
    @Mock
    private ProjectService projectService;
    
    @InjectMocks
    private ProjectController projectController;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testGetAllProjects() {
        // Arrange
        Project project1 = new Project();
        project1.setID(1);
        project1.setName("Project Alpha");
        
        Project project2 = new Project();
        project2.setID(2);
        project2.setName("Project Beta");
        
        List<Project> expectedProjects = Arrays.asList(project1, project2);
        
        when(projectService.findAll()).thenReturn(expectedProjects);
        
        // Act
        List<Project> actualProjects = projectController.getAllProjects();
        
        // Assert
        assertEquals(expectedProjects, actualProjects);
        verify(projectService, times(1)).findAll();
    }
    
    @Test
    public void testGetProjectById_Success() {
        // Arrange
        int projectId = 1;
        Project expectedProject = new Project();
        expectedProject.setID(projectId);
        expectedProject.setName("Project Alpha");
        
        when(projectService.getProjectById(projectId)).thenReturn(expectedProject);
        
        // Act
        ResponseEntity<Project> response = projectController.getProjectById(projectId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProject, response.getBody());
        verify(projectService, times(1)).getProjectById(projectId);
    }
    
    @Test
    public void testGetProjectById_NotFound() {
        // Arrange
        int projectId = 99;
        when(projectService.getProjectById(projectId)).thenThrow(new RuntimeException("Project not found"));
        
        // Act
        ResponseEntity<Project> response = projectController.getProjectById(projectId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(projectService, times(1)).getProjectById(projectId);
    }
    
    @Test
    public void testAddProject_Success() throws Exception {
        // Arrange
        Project newProject = new Project();
        newProject.setName("New Project");
        
        Project savedProject = new Project();
        savedProject.setID(3);
        savedProject.setName("New Project");
        
        when(projectService.addProject(newProject)).thenReturn(savedProject);
        
        // Act
        ResponseEntity response = projectController.addProject(newProject);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(projectService, times(1)).addProject(newProject);
        assertEquals("3", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
    }
    
    @Test
    public void testUpdateProject_Success() {
        // Arrange
        int projectId = 1;
        Project projectToUpdate = new Project();
        projectToUpdate.setID(projectId);
        projectToUpdate.setName("Updated Project");
        
        Project updatedProject = new Project();
        updatedProject.setID(projectId);
        updatedProject.setName("Updated Project");
        
        when(projectService.updateProject(projectId, projectToUpdate)).thenReturn(updatedProject);
        
        // Act
        ResponseEntity response = projectController.updateProject(projectToUpdate, projectId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProject, response.getBody());
        verify(projectService, times(1)).updateProject(projectId, projectToUpdate);
    }
    
    @Test
    public void testUpdateProject_NotFound() {
        // Arrange
        int projectId = 99;
        Project projectToUpdate = new Project();
        projectToUpdate.setID(projectId);
        projectToUpdate.setName("Updated Project");
        
        when(projectService.updateProject(projectId, projectToUpdate)).thenThrow(new RuntimeException("Project not found"));
        
        // Act
        ResponseEntity response = projectController.updateProject(projectToUpdate, projectId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(projectService, times(1)).updateProject(projectId, projectToUpdate);
    }
    
    @Test
    public void testDeleteProject_Success() {
        // Arrange
        int projectId = 1;
        when(projectService.deleteProject(projectId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = projectController.deleteProject(projectId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(projectService, times(1)).deleteProject(projectId);
    }
    
    @Test
    public void testDeleteProject_NotFound() {
        // Arrange
        int projectId = 99;
        when(projectService.deleteProject(projectId)).thenThrow(new RuntimeException("Project not found"));
        
        // Act
        ResponseEntity<Boolean> response = projectController.deleteProject(projectId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(projectService, times(1)).deleteProject(projectId);
    }
}