package com.framja.itss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        System.out.println(name);
        return "Hello, " + name + "!";
    }
} 