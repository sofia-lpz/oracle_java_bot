package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.ToDoItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface KpiRepository extends JpaRepository<Kpi,Integer> {

    @Query("SELECT t FROM Kpi t WHERE " +
           "(coalesce(:userIds, null) IS NULL OR t.user.id IN :userIds) AND " +
           "(coalesce(:teamIds, null) IS NULL OR t.user.team.id IN :teamIds) AND " +
           "(coalesce(:projectIds, null) IS NULL OR t.project.id IN :projectIds) AND " +
           "(coalesce(:sprintIds, null) IS NULL OR t.sprint.id IN :sprintIds)")
    List<Kpi> getKpiSummary(
            @Param("userIds") List<Integer> userIds,
            @Param("teamIds") List<Integer> teamIds,
            @Param("projectIds") List<Integer> projectIds,
            @Param("sprintIds") List<Integer> sprintIds
    );
}