package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.repository.KpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KpiService {

    @Autowired
    private KpiRepository kpiRepository;

    public List<Kpi> getKpiSummary(Long userId, Long teamId, Long projectId, Long sprintId) {
        return kpiRepository.getKpiSummary(userId, teamId, projectId, sprintId)
                .orElseThrow(() -> new RuntimeException("KPI summary not found for the specified parameters"));
    }

    public List<Kpi> findAll() {
        List<Kpi> kpis = kpiRepository.findAll();
        return kpis;
    }

    public Kpi getKpiById(int id) {
        return kpiRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("KPI not found with id: " + id));
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