package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.service.StateService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class StateControllerTest {

    @Mock
    private StateService stateService;

    @InjectMocks
    private StateController stateController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStates() {
        // Arrange
        State state1 = new State();
        state1.setID(1);
        state1.setName("To Do");

        State state2 = new State();
        state2.setID(2);
        state2.setName("In Progress");

        List<State> expectedStates = Arrays.asList(state1, state2);

        when(stateService.findAll()).thenReturn(expectedStates);

        // Act
        List<State> actualStates = stateController.getAllStates();

        // Assert
        assertEquals(expectedStates, actualStates);
        assertEquals(2, actualStates.size());
        verify(stateService, times(1)).findAll();
    }

    @Test
    public void testGetStateById_Success() {
        // Arrange
        int stateId = 1;
        State expectedState = new State();
        expectedState.setID(stateId);
        expectedState.setName("To Do");
        
        when(stateService.getStateById(stateId)).thenReturn(expectedState);
        
        // Act
        ResponseEntity<State> response = stateController.getStateById(stateId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedState, response.getBody());
        verify(stateService, times(1)).getStateById(stateId);
    }

    @Test
    public void testGetStateById_NotFound() {
        // Arrange
        int stateId = 99;
        when(stateService.getStateById(stateId)).thenThrow(new RuntimeException("State not found"));
        
        // Act
        ResponseEntity<State> response = stateController.getStateById(stateId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(stateService, times(1)).getStateById(stateId);
    }

    @Test
    public void testAddState_Success() throws Exception {
        // Arrange
        State newState = new State();
        newState.setName("New State");
        
        State savedState = new State();
        savedState.setID(3);
        savedState.setName("New State");
        
        when(stateService.addState(newState)).thenReturn(savedState);
        
        // Act
        ResponseEntity response = stateController.addState(newState);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(stateService, times(1)).addState(newState);
        assertEquals("3", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
    }

    @Test
    public void testUpdateState_Success() {
        // Arrange
        int stateId = 1;
        State stateToUpdate = new State();
        stateToUpdate.setID(stateId);
        stateToUpdate.setName("Updated State");
        
        State updatedState = new State();
        updatedState.setID(stateId);
        updatedState.setName("Updated State");
        
        when(stateService.updateState(stateId, stateToUpdate)).thenReturn(updatedState);
        
        // Act
        ResponseEntity response = stateController.updateState(stateToUpdate, stateId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedState, response.getBody());
        verify(stateService, times(1)).updateState(stateId, stateToUpdate);
    }

    @Test
    public void testUpdateState_NotFound() {
        // Arrange
        int stateId = 99;
        State stateToUpdate = new State();
        stateToUpdate.setID(stateId);
        stateToUpdate.setName("Updated State");
        
        when(stateService.updateState(stateId, stateToUpdate)).thenThrow(new RuntimeException("State not found"));
        
        // Act
        ResponseEntity response = stateController.updateState(stateToUpdate, stateId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(stateService, times(1)).updateState(stateId, stateToUpdate);
    }

    @Test
    public void testDeleteState_Success() {
        // Arrange
        int stateId = 1;
        when(stateService.deleteState(stateId)).thenReturn(true);
        
        // Act
        ResponseEntity<Boolean> response = stateController.deleteState(stateId);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(stateService, times(1)).deleteState(stateId);
    }

    @Test
    public void testDeleteState_NotFound() {
        // Arrange
        int stateId = 99;
        when(stateService.deleteState(stateId)).thenThrow(new RuntimeException("State not found"));
        
        // Act
        ResponseEntity<Boolean> response = stateController.deleteState(stateId);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(stateService, times(1)).deleteState(stateId);
    }
}