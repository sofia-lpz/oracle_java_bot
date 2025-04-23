package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public State getStateById(int id) {
        return stateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("State not found with id: " + id));
    }
    
    public State getStateByName(String name) {
        return stateRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("State not found with name: " + name));
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
