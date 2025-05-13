package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.Kpi;
import com.springboot.MyTodoList.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {

    List<ToDoItem> getToDoItemsByUserID(int userId);

    @Query("SELECT t FROM ToDoItem t WHERE " +
            "(:userIds IS EMPTY OR t.user.id IN :userIds) AND " +
            "(:teamIds IS EMPTY OR t.user.team.id IN :teamIds) AND " +
            "(:projectIds IS EMPTY OR t.project.id IN :projectIds) AND " +
            "(:sprintIds IS EMPTY OR t.sprint.id IN :sprintIds) AND " +
            "(:done IS NULL OR t.done = :done)")
    List<ToDoItem> getToDoItemsSummary(
            @Param("userIds") List<Integer> userIds,
            @Param("teamIds") List<Integer> teamIds,
            @Param("projectIds") List<Integer> projectIds,
            @Param("sprintIds") List<Integer> sprintIds,
            @Param("done") Boolean done);

}
