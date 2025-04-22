package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.service.KpiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class KpiController {
    @Autowired
    private KpiService kpiService;

    @GetMapping(value = "/kpi/summary")
    public ResponseEntity<List<Kpi>> getKpiSummary(@RequestParam(required = false) Long userId,
                                                  @RequestParam(required = false) Long teamId,
                                                  @RequestParam(required = false) Long projectId,
                                                  @RequestParam(required = false) Long sprintId) {
        try {
            List<Kpi> kpis = kpiService.getKpiSummary(userId, teamId, projectId, sprintId);
            return ResponseEntity.ok(kpis);
        } catch (RuntimeException e) {
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