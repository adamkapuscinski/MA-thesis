package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import com.mathesis.loadbalancer.domain.TransmissionModel;
import com.mathesis.loadbalancer.domain.chart.ChartDataModel;
import com.mathesis.loadbalancer.domain.chart.ChartSetDataModel;
import com.mathesis.loadbalancer.domain.chart.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("")
public class LBController {
    private final LBProvider lbProvider;

    LBController(LBProvider lbProvider) {
        this.lbProvider = lbProvider;
    }

    @GetMapping("api/call")
    public ResponseEntity<Object> call() {
        return ResponseEntity.ok().body(new TransmissionModel(callSomeService()));
    }
    @GetMapping
    public String callSomeService() {
        return lbProvider.callSomeService();
    }

    @GetMapping("api/stats")
    public ChartSetDataModel getStatistics() {
        return lbProvider.assignDataObject();
    }
}
