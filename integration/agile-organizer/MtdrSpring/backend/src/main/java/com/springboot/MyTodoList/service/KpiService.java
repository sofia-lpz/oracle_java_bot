package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.repository.KpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KpiService {

    @Autowired
    private KpiRepository kpiRepository;

    public ResponseEntity<List<Kpi>> getKpiSummary(Long userId, Long teamId, Long projectId, Long sprintId) {
        Optional<List<Kpi>> kpiList = kpiRepository.getKpiSummary(userId, teamId, projectId, sprintId);
        if (kpiList.isPresent()){
            return new ResponseEntity<>(kpiList.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public List<Kpi> findAll() {
        List<Kpi> kpis = kpiRepository.findAll();
        return kpis;
    }

    public ResponseEntity<Kpi> getKpiById(Long id) {
        Optional<Kpi> kpiData = kpiRepository.findById(id.intValue());
        if (kpiData.isPresent()) {
            return new ResponseEntity<>(kpiData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Kpi addKpi(Kpi kpi) {
        return kpiRepository.save(kpi);
    }

    public boolean deleteKpi(Long id) {
        try {
            kpiRepository.deleteById(id.intValue());
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public Kpi updateKpi(Long id, Kpi kpi) {
        Optional<Kpi> kpiData = kpiRepository.findById(id.intValue());
        if(kpiData.isPresent()) {
            Kpi existingKpi = kpiData.get();
            
            if(kpi.getType() != null) {
                existingKpi.setType(kpi.getType());
            }
            
            if(kpi.getTotal() != null) {
                existingKpi.setTotal(kpi.getTotal());
            }
            
            if(kpi.getSum() != null) {
                existingKpi.setSum(kpi.getSum());
            }
            
            if(kpi.getUser() != null) {
                existingKpi.setUser(kpi.getUser());
            }
            
            if(kpi.getTeam() != null) {
                existingKpi.setTeam(kpi.getTeam());
            }
            
            if(kpi.getProject() != null) {
                existingKpi.setProject(kpi.getProject());
            }
            
            if(kpi.getSprint() != null) {
                existingKpi.setSprint(kpi.getSprint());
            }
            
            return kpiRepository.save(existingKpi);
        } else {
            return null;
        }
    }
}