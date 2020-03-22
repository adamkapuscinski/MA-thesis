package com.mathesis.serviceone.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("")
public class ServiceOneController {
    @Value("${server.port}")
    String serverPort;

    @GetMapping
    public String serviceCall() {
        log.info("ServiceOne Caled");
        log.warn("DEBUG: this is a debug info");
        return "<h1 style=\"color: red\">Welcome in service one</h1><h3>Currently running on port:" + serverPort + "</h3>";
    }
}
