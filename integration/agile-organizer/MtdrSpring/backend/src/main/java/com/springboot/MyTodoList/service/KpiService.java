package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.controller.Util.GroupBy;
import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.repository.KpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KpiService {
    private static final Logger logger = LoggerFactory.getLogger(KpiService.class);

    @Autowired
    private KpiRepository kpiRepository;

    public List<Kpi> getKpiSummary(Integer userId, Integer teamId, Integer projectId, Integer sprintId) {
        logger.info("Service: Fetching KPI summary with userId={}, teamId={}, projectId={}, sprintId={}", 
                   userId, teamId, projectId, sprintId);
        try {
            Optional<List<Kpi>> kpisOptional = kpiRepository.getKpiSummary(userId, teamId, projectId, sprintId);
            List<Kpi> result = kpisOptional.orElse(Collections.emptyList());
            logger.info("Found {} KPIs in repository", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error in KPI repository query: {}", e.getMessage(), e);
            // Returning empty list instead of throwing exception
            return Collections.emptyList();
        }
    }

    public List<Kpi> getKpiOther(GroupBy groupBy) {
        try {
            List<Kpi> kpis = kpiRepository.getKpiOther(groupBy);
            return kpis;
        } catch (Exception e) {
            return Collections.emptyList();
        }
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