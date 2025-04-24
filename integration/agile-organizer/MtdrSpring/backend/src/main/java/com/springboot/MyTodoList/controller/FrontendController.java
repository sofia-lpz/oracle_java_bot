package com.springboot.MyTodoList.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = { 
        "/", 
        "/login",
        "/dashboard", 
        "/task", "/Task",
        "/users", "/Users",  
        "/chatbot", "/Chatbot",
        "/home"
    })
    public String forward() {
        return "forward:/index.html";
    }
}