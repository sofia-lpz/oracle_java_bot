package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.service.KpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class KpiController {
    private static final Logger logger = LoggerFactory.getLogger(KpiController.class);

    @Autowired
    private KpiService kpiService;

    @GetMapping(value = "/kpi/summary")
    public ResponseEntity<List<Kpi>> getKpiSummary(@RequestParam(required = false) Integer userId,
                                                  @RequestParam(required = false) Integer teamId,
                                                  @RequestParam(required = false) Integer projectId,
                                                  @RequestParam(required = false) Integer sprintId) {
        logger.info("Requesting KPI summary with userId={}, teamId={}, projectId={}, sprintId={}", 
                   userId, teamId, projectId, sprintId);
        try {
            List<Kpi> kpis = kpiService.getKpiSummary(userId, teamId, projectId, sprintId);
            logger.info("Found {} KPIs matching criteria", kpis.size());
            return ResponseEntity.ok(kpis);
        } catch (RuntimeException e) {
            logger.error("Error retrieving KPI summary: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/kpi")
    public List<Kpi> getAllKpis(){
        return kpiService.findAll();
    }
    
    @GetMapping(value = "/kpi/{id}")
    public ResponseEntity<Kpi> getKpiById(@PathVariable int id){
        try{
            Kpi kpi = kpiService.getKpiById(id);
            return new ResponseEntity<>(kpi, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(value = "/kpi")
    public ResponseEntity addKpi(@RequestBody Kpi kpi) throws Exception{
        Kpi savedKpi = kpiService.addKpi(kpi);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+savedKpi.getID());
        responseHeaders.set("Access-Control-Expose-Headers","location");

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }
    
    @PutMapping(value = "/kpi/{id}")
    public ResponseEntity updateKpi(@RequestBody Kpi kpi, @PathVariable Long id){
        try{
            Kpi updatedKpi = kpiService.updateKpi(id, kpi);
            return new ResponseEntity<>(updatedKpi, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/kpi/{id}")
    public ResponseEntity<Boolean> deleteKpi(@PathVariable Long id){
        Boolean flag = false;
        try{
            flag = kpiService.deleteKpi(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }
}