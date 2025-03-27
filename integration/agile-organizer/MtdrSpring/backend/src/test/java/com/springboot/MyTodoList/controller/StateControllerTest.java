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
import static org.mockito.Mockito.*;

public class StateControllerTest {

    @Mock
    private StateService stateService;

    @InjectMocks
    private StateController stateController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllStates() {
        State state1 = new State();
        State state2 = new State();
        List<State> stateList = Arrays.asList(state1, state2);

        when(stateService.findAll()).thenReturn(stateList);

        List<State> result = stateController.getAllStates();
        assertEquals(2, result.size());
        verify(stateService, times(1)).findAll();
    }

    @Test
    public void testGetStateById() {
        State state = new State();
        when(stateService.getStateById(1)).thenReturn(new ResponseEntity<>(state, HttpStatus.OK));

        ResponseEntity<State> result = stateController.getStateById(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(stateService, times(1)).getStateById(1);
    }

    @Test
    public void testGetStateByIdNotFound() {
        when(stateService.getStateById(1)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<State> result = stateController.getStateById(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(stateService, times(1)).getStateById(1);
    }

    @Test
    public void testAddState() throws Exception {
        State state = new State();
        when(stateService.addState(state)).thenReturn(state);

        ResponseEntity result = stateController.addState(state);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(stateService, times(1)).addState(state);
    }

    @Test
    public void testUpdateState() {
        State state = new State();
        when(stateService.updateState(1, state)).thenReturn(state);

        ResponseEntity result = stateController.updateState(state, 1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(stateService, times(1)).updateState(1, state);
    }

    @Test
    public void testUpdateStateNotFound() {
        State state = new State();
        when(stateService.updateState(1, state)).thenReturn(null);

        ResponseEntity result = stateController.updateState(state, 1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(stateService, times(1)).updateState(1, state);
    }

    @Test
    public void testDeleteState() {
        when(stateService.deleteState(1)).thenReturn(true);

        ResponseEntity<Boolean> result = stateController.deleteState(1);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(true, result.getBody());
        verify(stateService, times(1)).deleteState(1);
    }

    @Test
    public void testDeleteStateNotFound() {
        when(stateService.deleteState(1)).thenReturn(false);

        ResponseEntity<Boolean> result = stateController.deleteState(1);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(false, result.getBody());
        verify(stateService, times(1)).deleteState(1);
    }
}