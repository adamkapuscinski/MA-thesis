package com.mathesis.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class HystrixController {
    @GetMapping("/first")
    public String firstServiceFallback() {
        return "<h1 style=\"display: 3\">Something went wrong with service one. Maybe is down ?</h1>";
    }
    @GetMapping("/second")
    public String secondServiceFallback() {
        return "<h1 style=\"display: 3\">Something went wrong with service two. Maybe is down ? </h1>";
    }
}
