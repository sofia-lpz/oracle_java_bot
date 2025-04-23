package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;
import java.util.Optional;
@Repository
@Transactional
@EnableTransactionManagement
public interface StateRepository extends JpaRepository<State,Integer> {
    Optional<State> findByName(String name);
}