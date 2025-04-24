package com.springboot.MyTodoList.controller;

import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.service.LupitaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class LupitaController {
    @Autowired
    private LupitaService lupitaService;

    @PostMapping("/api/chat")
    public ResponseEntity<String> chat(@RequestBody String prompt) {
        try {
            String response = lupitaService.chat(prompt);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
