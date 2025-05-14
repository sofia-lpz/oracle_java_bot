package com.springboot.MyTodoList;

import com.springboot.MyTodoList.controller.ToDoItemController;
import com.springboot.MyTodoList.controller.KpiController;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.service.KpiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Release12Test {

    @Mock
    private ToDoItemService toDoItemService;
    @Mock
    private KpiService kpiService;

    @InjectMocks
    private ToDoItemController toDoItemController;
    @InjectMocks
    private KpiController kpiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateItem() throws Exception {
        // Arrange
        User user = new User();
        user.setID(1);
        user.setName("John Doe");

        Project project = new Project();
        project.setID(1);
        project.setName("Agile Project");

        Sprint sprint = new Sprint();
        sprint.setID(1);
        sprint.setName("Sprint 1");

        State state = new State();
        state.setID(1);
        state.setName("TO_DO");

        ToDoItem newTask = new ToDoItem();
        newTask.setTitle("Nueva Tarea de Prueba");
        newTask.setDescription("Descripción de la tarea de prueba");
        newTask.setUser(user);
        newTask.setProject(project);
        newTask.setSprint(sprint);
        newTask.setState(state);
        newTask.setEstimatedHours(5);
        newTask.setStoryPoints(3);
        newTask.setPriority("Medium");
        newTask.setDone(false);

        ToDoItem savedTask = new ToDoItem();
        savedTask.setID(1);
        savedTask.setTitle("Nueva Tarea de Prueba");
        savedTask.setDescription("Descripción de la tarea de prueba");
        savedTask.setUser(user);
        savedTask.setProject(project);
        savedTask.setSprint(sprint);
        savedTask.setState(state);
        savedTask.setEstimatedHours(5);
        savedTask.setStoryPoints(3);
        savedTask.setPriority("Medium");
        savedTask.setDone(false);

        when(toDoItemService.addToDoItem(newTask)).thenReturn(savedTask);

        // Act
        ResponseEntity result = toDoItemController.addToDoItem(newTask);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(toDoItemService, times(1)).addToDoItem(newTask);
        assertEquals("1", result.getHeaders().get("location").get(0));
        assertEquals("location", result.getHeaders().get("Access-Control-Expose-Headers").get(0));
    }

    @Test
    public void testGetKpiSummary_SpecifiedSprint() {
        // Arrange
        Integer sprintId = 4;
        Integer userId = null;
        Integer teamId = null;
        Integer projectId = null;

        Kpi kpi1 = new Kpi();
        kpi1.setID(1);
        kpi1.setType("VISIBILITY");

        Kpi kpi2 = new Kpi();
        kpi2.setID(2);
        kpi2.setType("ACCOUNTABILITY");

        List<Kpi> expectedKpis = Arrays.asList(kpi1, kpi2);

        List<Integer> userIdList = userId != null ? List.of(userId) : null;
        List<Integer> teamIdList = teamId != null ? List.of(teamId) : null;
        List<Integer> projectIdList = projectId != null ? List.of(projectId) : null;
        List<Integer> sprintIdList = sprintId != null ? List.of(sprintId) : null;

        when(kpiService.getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList)).thenReturn(expectedKpis);

        // Act
        ResponseEntity<List<Kpi>> response = kpiController.getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKpis, response.getBody());
        verify(kpiService, times(1)).getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList);
    }

    @Test
    public void testGetKpiSummary_SpecifiedSprintAndUser() {
        // Arrange
        Integer userId = 1;
        Integer sprintId = 4;
        // Passing only userId and sprintId, other parameters as null
        Integer teamId = null;
        Integer projectId = null;

        Kpi kpi1 = new Kpi();
        kpi1.setID(1);
        kpi1.setType("VISIBILITY");

        Kpi kpi2 = new Kpi();
        kpi2.setID(2);
        kpi2.setType("ACCOUNTABILITY");

        List<Kpi> expectedKpis = Arrays.asList(kpi1, kpi2);

        List<Integer> userIdList = userId != null ? List.of(userId) : null;
        List<Integer> teamIdList = teamId != null ? List.of(teamId) : null;
        List<Integer> projectIdList = projectId != null ? List.of(projectId) : null;
        List<Integer> sprintIdList = sprintId != null ? List.of(sprintId) : null;

        when(kpiService.getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList)).thenReturn(expectedKpis);

        // Act
        ResponseEntity<List<Kpi>> response = kpiController.getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKpis, response.getBody());
        verify(kpiService, times(1)).getKpiSummary(userIdList, teamIdList, projectIdList, sprintIdList);
    }

    @Test
    public void testGetToDoItemsSummary_SpecificSprint_DoneItems() {
        // Arrange
        Integer sprintId = 3;
        Boolean done = true;
        
        // Convert to List<Integer>
        List<Integer> sprintIds = sprintId != null ? Collections.singletonList(sprintId) : null;

        ToDoItem item1 = new ToDoItem();
        item1.setID(1);
        item1.setTitle("Sprint Task 1");
        item1.setDone(true);

        ToDoItem item2 = new ToDoItem();
        item2.setID(2);
        item2.setTitle("Sprint Task 2");
        item2.setDone(true);

        List<ToDoItem> expectedItems = Arrays.asList(item1, item2);

        when(toDoItemService.getToDoItemsSummary(null, null, null, sprintIds, done))
                .thenReturn(expectedItems);

        // Act
        ResponseEntity<List<ToDoItem>> response = toDoItemController.getToDoItemsSummary(null, null, null, sprintIds, done);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(toDoItemService, times(1)).getToDoItemsSummary(null, null, null, sprintIds, done);
    }

    @Test
    public void testGetToDoItemsSummary_SpecificSprintAndUser_DoneItems() {
        // Arrange
        Integer userId = 1;
        Integer sprintId = 3;
        Boolean done = true;
        
        // Convert to List<Integer>
        List<Integer> userIds = userId != null ? Collections.singletonList(userId) : null;
        List<Integer> sprintIds = sprintId != null ? Collections.singletonList(sprintId) : null;

        ToDoItem item1 = new ToDoItem();
        item1.setID(1);
        item1.setTitle("Completed Sprint Task");
        item1.setDone(true);

        List<ToDoItem> expectedItems = Arrays.asList(item1);

        when(toDoItemService.getToDoItemsSummary(userIds, null, null, sprintIds, done))
                .thenReturn(expectedItems);

        // Act
        ResponseEntity<List<ToDoItem>> response = toDoItemController.getToDoItemsSummary(userIds, null, null, sprintIds, done);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedItems, response.getBody());
        verify(toDoItemService, times(1)).getToDoItemsSummary(userIds, null, null, sprintIds, done);
    }
}