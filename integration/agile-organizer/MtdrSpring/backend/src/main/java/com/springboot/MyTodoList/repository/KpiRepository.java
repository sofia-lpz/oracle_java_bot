package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.Kpi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface KpiRepository extends JpaRepository<Kpi,Integer> {

    @Query("SELECT k FROM Kpi k WHERE (:userId is null OR k.user.id = :userId) AND (:teamId is null OR k.team.id = :teamId) AND (:projectId is null OR k.project.id = :projectId) AND (:sprintId is null OR k.sprint.id = :sprintId)")
    Optional<List<Kpi>> getKpiSummary(@Param("userId") Long userId, @Param("teamId") Long teamId, @Param("projectId") Long projectId, @Param("sprintId") Long sprintId);

}