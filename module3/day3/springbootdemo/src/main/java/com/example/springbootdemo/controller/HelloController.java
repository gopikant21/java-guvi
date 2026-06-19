package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!!";
    }

    @RequestMapping("/")
    public String welcome(){
        return "Welcome to rest api using Spring Boot!";
    }

    @RequestMapping("/bye")
    public String bye(){
        return "Bye!!";
    }
}
