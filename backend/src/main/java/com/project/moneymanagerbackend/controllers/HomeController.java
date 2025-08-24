package com.project.moneymanagerbackend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status", "/about"})
public class HomeController {

    /** The page will show "Hello World!" in the direction "http://localhost:8080/api/v1.0/status/" */
    @GetMapping
    public String helloCheck(){
        return "Hello World!";
    }

}
