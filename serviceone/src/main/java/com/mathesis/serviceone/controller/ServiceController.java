package com.mathesis.serviceone.controller;

import com.mathesis.serviceone.controller.matrix.Matrix;
import com.mathesis.serviceone.controller.matrix.MatrixCalcProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("")
public class ServiceController {
    @Value("${server.port}")
    String serverPort;
    private Long param = 50L;
    @Value("${spring.application.name}")
    String applicationName;

    @GetMapping
    public String serviceCall() throws InterruptedException {
        log.info(applicationName + " Called");
        log.warn("DEBUG: this is a debug info");
        Thread.sleep(param);
        return "<h1 style=\"color: red\">Welcome in " + applicationName + "</h1><h3>Currently running on port:" + serverPort + "</h3>";
    }
    @GetMapping("/random-burden")
    public String transposeRandomMatrix() {
        log.info("ServiceOne Called");
        Matrix matrix = MatrixCalcProvider.transposeRandomMatrix();
        return "<h1 style=\"color: red\">Welcome in " + applicationName + "</h1><h3>Currently running on port:" + serverPort + "</h3><p>I've been calculating transpose of matrix(rows: " + matrix.getNrows() + " cols: " + matrix.getNcols() + ")</p>";
    }
    @GetMapping("/param/{param}")
    public String assignParamToController(@PathVariable Long param) {
        this.param = param;
        return "<h1 style=\"color: red\">Welcome in " + applicationName + "</h1><h3>Currently running on port:" + serverPort + ".</h3><h5>I've properly assigned param to " + this.param + "</h5>";

    }
}
