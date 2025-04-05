package com.springboot.MyTodoList.controller;


import com.springboot.MyTodoList.model.State;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
public class StateController {
    @Autowired
    private StateService stateService;

    //@CrossOrigin
    @GetMapping(value = "/states")
    public List<State> getAllStates(){
        return stateService.findAll();
    }

    //@CrossOrigin
    @GetMapping(value = "/states/{id}")
    public ResponseEntity<State> getStateById(@PathVariable int id){
        try{
            ResponseEntity<State> responseEntity = stateService.getStateById(id);
            return new ResponseEntity<State>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/states")
    public ResponseEntity addState(@RequestBody State state) throws Exception{
        State st = stateService.addState(state);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+st.getID());
        responseHeaders.set("Access-Control-Expose-Headers","location");
        //URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    @PutMapping(value = "states/{id}")
    public ResponseEntity updateState(@RequestBody State state, @PathVariable int id){
        try{
            State state1 = stateService.updateState(id, state);
            System.out.println(state1.toString());
            return new ResponseEntity<>(state1,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //@CrossOrigin
    @DeleteMapping(value = "states/{id}")
    public ResponseEntity<Boolean> deleteState(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = stateService.deleteState(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }

}
