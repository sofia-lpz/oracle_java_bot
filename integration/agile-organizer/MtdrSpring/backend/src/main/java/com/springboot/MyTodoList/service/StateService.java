package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> findAll(){
        List<State> states = stateRepository.findAll();
        return states;
    }

    public ResponseEntity<State> getStateById(int id){
        Optional<State> stateData = stateRepository.findById(id);
        if (stateData.isPresent()){
            return new ResponseEntity<>(stateData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public State addState(State state){
        return stateRepository.save(state);
    }

    public boolean deleteState(int id){
        try{
            stateRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public State updateState(int id, State state){
        Optional<State> stateData = stateRepository.findById(id);
        if(stateData.isPresent()){
            State state1 = stateData.get();
            state1.setID(id);
            state1.setName(state.getName());
            return stateRepository.save(state1);
        }else{
            return null;
        }
    }
    
}
