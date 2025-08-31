package com.teachermanagement.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    
    @GetMapping("/api/test")
    public String test() {
        return "Backend is working!";
    }
    
    @GetMapping("/api/test/ping")
    public String ping() {
        return "pong";
    }
}