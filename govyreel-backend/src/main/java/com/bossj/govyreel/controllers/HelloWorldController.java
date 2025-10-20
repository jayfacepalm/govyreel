package com.bossj.govyreel.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/hello")   
public class HelloWorldController {
    
    @GetMapping("/getMethodName")
    public String getMethodName() {
        return "Yo! Welcome to GoVyReel Backend!";
    } 
}
