package com.mathesis.serviceone.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("")
public class ServiceOneController {
    @Value("${server.port}")
    String serverPort;
    private Long param = 50L;

    @GetMapping
    public String serviceCall() throws InterruptedException {
        log.info("ServiceOne Called");
        log.warn("DEBUG: this is a debug info");
        Thread.sleep(param);
        return "<h1 style=\"color: red\">Welcome in service one</h1><h3>Currently running on port:" + serverPort + "</h3>";
    }
    @GetMapping("/{param}")
    public String assignParamToController(@PathVariable Long param) {
        this.param = param;
        return "<h1 style=\"color: red\">Welcome in service one</h1><h3>Currently running on port:" + serverPort + ".</h3><h5>I've properly assigned param to" + this.param + "</h5>";

    }
}
