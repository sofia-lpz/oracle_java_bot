package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface KpiRepository extends JpaRepository<Kpi,Integer> {

    @Query("SELECT k FROM Kpi k WHERE (:userId is null OR k.userId = :userId) AND (:teamId is null OR k.teamId = :teamId) AND (:projectId is null OR k.projectId = :projectId) AND(:sprintId is null OR k.sprintId = :sprintId)")
    Optional<List<Kpi>> getKpiSummary(Long userId, Long teamId, Long projectId, Long sprintId);

}
