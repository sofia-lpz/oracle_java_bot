package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.model.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class KpiControllerTest {

    @Mock
    private KpiService kpiService;

    @InjectMocks
    private KpiController kpiController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    /*

    @Test
    public void testGetAllKpis() {
        // Arrange
        Kpi kpi1 = new Kpi();
        kpi1.setID(1);
        kpi1.setType("VISIBILITY");
        kpi1.setSum(75);
        kpi1.setTotal(100);

        Kpi kpi2 = new Kpi();
        kpi2.setID(2);
        kpi2.setType("ACCOUNTABILITY");
        kpi2.setSum(80);
        kpi2.setTotal(100);

        List<Kpi> expectedKpis = Arrays.asList(kpi1, kpi2);

        when(kpiService.findAll()).thenReturn(expectedKpis);

        // Act
        List<Kpi> actualKpis = kpiController.getAllKpis();

        // Assert
        assertEquals(expectedKpis, actualKpis);
        assertEquals(2, actualKpis.size());
        verify(kpiService, times(1)).findAll();
    }

    @Test
    public void testGetKpiById_Success() {
        // Arrange
        int kpiId = 1;
        Kpi expectedKpi = new Kpi();
        expectedKpi.setID(kpiId);
        expectedKpi.setType("PRODUCTIVITY");
        expectedKpi.setSum(40);
        expectedKpi.setTotal(50);

        when(kpiService.getKpiById(kpiId)).thenReturn(expectedKpi);

        // Act
        ResponseEntity<Kpi> response = kpiController.getKpiById(kpiId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKpi, response.getBody());
        verify(kpiService, times(1)).getKpiById(kpiId);
    }

    @Test
    public void testGetKpiById_NotFound() {
        // Arrange
        int kpiId = 999;
        when(kpiService.getKpiById(kpiId)).thenThrow(new RuntimeException("KPI not found"));

        // Act
        ResponseEntity<Kpi> response = kpiController.getKpiById(kpiId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(kpiService, times(1)).getKpiById(kpiId);
    }

    @Test
    public void testGetKpiSummary_Success() {
        // Arrange
        Integer userId = 1;
        Integer teamId = 2;
        Integer projectId = 3;
        Integer sprintId = 4;

        Kpi kpi1 = new Kpi();
        kpi1.setID(1);
        kpi1.setType("VISIBILITY");

        Kpi kpi2 = new Kpi();
        kpi2.setID(2);
        kpi2.setType("ACCOUNTABILITY");

        List<Kpi> expectedKpis = Arrays.asList(kpi1, kpi2);

        when(kpiService.getKpiSummary(userId, teamId, projectId, sprintId)).thenReturn(expectedKpis);

        // Act
        ResponseEntity<List<Kpi>> response = kpiController.getKpiSummary(userId, teamId, projectId, sprintId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedKpis, response.getBody());
        verify(kpiService, times(1)).getKpiSummary(userId, teamId, projectId, sprintId);
    }

    @Test
    public void testGetKpiSummary_Error() {
        // Arrange
        Integer userId = 1;
        when(kpiService.getKpiSummary(userId, null, null, null))
                .thenThrow(new RuntimeException("Error retrieving KPI summary"));

        // Act
        ResponseEntity<List<Kpi>> response = kpiController.getKpiSummary(userId, null, null, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(kpiService, times(1)).getKpiSummary(userId, null, null, null);
    }

    @Test
    public void testAddKpi_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setID(1);

        Team team = new Team();
        team.setID(2);

        Project project = new Project();
        project.setID(3);

        Sprint sprint = new Sprint();
        sprint.setID(4);

        Kpi newKpi = new Kpi();
        newKpi.setType("VISIBILITY");
        newKpi.setSum(85);
        newKpi.setTotal(100);
        newKpi.setUser(user);
        newKpi.setTeam(team);
        newKpi.setProject(project);
        newKpi.setSprint(sprint);

        Kpi savedKpi = new Kpi();
        savedKpi.setID(1);
        savedKpi.setType("VISIBILITY");
        savedKpi.setSum(85);
        savedKpi.setTotal(100);
        savedKpi.setUser(user);
        savedKpi.setTeam(team);
        savedKpi.setProject(project);
        savedKpi.setSprint(sprint);

        when(kpiService.addKpi(newKpi)).thenReturn(savedKpi);

        // Act
        ResponseEntity response = kpiController.addKpi(newKpi);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getHeaders().get("location"));
        assertEquals("1", response.getHeaders().get("location").get(0));
        assertEquals("location", response.getHeaders().get("Access-Control-Expose-Headers").get(0));
        verify(kpiService, times(1)).addKpi(newKpi);
    }

    @Test
    public void testUpdateKpi_Success() {
        // Arrange
        Long kpiId = 1L;
        Kpi kpiToUpdate = new Kpi();
        kpiToUpdate.setID(1);
        kpiToUpdate.setType("VISIBILITY");
        kpiToUpdate.setSum(90);
        kpiToUpdate.setTotal(100);

        Kpi updatedKpi = new Kpi();
        updatedKpi.setID(1);
        updatedKpi.setType("VISIBILITY");
        updatedKpi.setSum(90);
        updatedKpi.setTotal(100);

        when(kpiService.updateKpi(kpiId, kpiToUpdate)).thenReturn(updatedKpi);

        // Act
        ResponseEntity response = kpiController.updateKpi(kpiToUpdate, kpiId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedKpi, response.getBody());
        verify(kpiService, times(1)).updateKpi(kpiId, kpiToUpdate);
    }

    @Test
    public void testUpdateKpi_NotFound() {
        // Arrange
        Long kpiId = 999L;
        Kpi kpiToUpdate = new Kpi();
        kpiToUpdate.setType("VISIBILITY");

        when(kpiService.updateKpi(kpiId, kpiToUpdate)).thenReturn(null);

        // Act
        ResponseEntity response = kpiController.updateKpi(kpiToUpdate, kpiId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(kpiService, times(1)).updateKpi(kpiId, kpiToUpdate);
    }

    @Test
    public void testDeleteKpi_Success() {
        // Arrange
        Long kpiId = 1L;
        when(kpiService.deleteKpi(kpiId)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = kpiController.deleteKpi(kpiId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
        verify(kpiService, times(1)).deleteKpi(kpiId);
    }

    @Test
    public void testDeleteKpi_NotFound() {
        // Arrange
        Long kpiId = 999L;
        when(kpiService.deleteKpi(kpiId)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = kpiController.deleteKpi(kpiId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, response.getBody());
        verify(kpiService, times(1)).deleteKpi(kpiId);
    }
        */
}