package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem,Integer> {

        List<ToDoItem> findByProjectId(int projectId);
    List<ToDoItem> findByUserId(int userId);
    List<ToDoItem> findBySprintId(int sprintId);
    List<ToDoItem> findByStateId(int stateId);
}
