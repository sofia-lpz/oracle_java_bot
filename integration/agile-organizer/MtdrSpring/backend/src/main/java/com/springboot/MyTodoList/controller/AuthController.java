package com.springboot.MyTodoList.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login exitoso");
        response.put("token", "fake-token-123456789");
        response.put("username", loginRequest.get("username"));
        return ResponseEntity.ok(response);
    }
} 