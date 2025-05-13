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

    @Query("SELECT t FROM ToDoItem t WHERE (:userId is null OR t.user.id = :userId) AND (:projectId is null OR t.project.id = :projectId) AND (:sprintId is null OR t.sprint.id = :sprintId) AND (:done is null OR t.done = :done)")
    List<ToDoItem> getToDoItemsSummary(Integer userId, Integer projectId, Integer sprintId,
            Boolean done);

}
