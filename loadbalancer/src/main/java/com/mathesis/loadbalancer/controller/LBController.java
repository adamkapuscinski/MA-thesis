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
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("")
public class LBController {
    private String[] registeredServices = new String[]{"SERVICEONE", "SERVICETWO", "SERVICETHREE"};
    private int serviceIdx = 0;

    private ChartDataModel data = new ChartDataModel();

    List<ServiceRequestData> recentRequestData = new ArrayList<>();
    private final LBProvider lbProvider;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    LBController(LBProvider lbProvider) {
        this.lbProvider = lbProvider;
    }

    @PostConstruct
    public void init() {
        this.data.setLabels(new ArrayList<>());
        List<DataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < registeredServices.length; i++) {
            DataSet dataSet = new DataSet();
            dataSet.setBorderColor(LBProvider.assignBorderColorBasedOnIdx(i));
            dataSet.setFill(true);
            dataSet.setLabel(registeredServices[i]);
            dataSet.setData(new ArrayList<>());
            dataSets.add(dataSet);
        }
        this.data.setDatasets(dataSets);
    }
    @GetMapping("api/call")
    public ResponseEntity<Object> call() {
        return ResponseEntity.ok().body(new TransmissionModel(callSomeService()));
    }
    @GetMapping
    public String callSomeService() {
        List<ServiceInstance> instances = this.discoveryClient.getInstances(registeredServices[serviceIdx]);
        String result = "";
        if (instances.size() > 0) {
            ServiceInstance serviceInstance = instances.get(0);
            Instant start = Instant.now();
            String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/";
            System.out.println("calling url: " + url);
            result = restTemplate.getForObject(url, String.class);
            Instant finish = Instant.now();
            assignParamsToMap(start, finish, registeredServices[serviceIdx]);
        }

        serviceIdx++;
        if (serviceIdx > 2) {
            serviceIdx = 0;
        }
        return result;
    }

    @GetMapping("api/stats")
    public ChartSetDataModel getStatistics() {
        return lbProvider.assignDataObject(registeredServices, recentRequestData);
    }

    private void assignParamsToMap(Instant start, Instant finish, String registeredService) {
        long duration = Duration.between(start, finish).toMillis();
        ServiceRequestData serviceRequestDataObj = new ServiceRequestData(start, registeredService, finish, duration);
        recentRequestData.add(serviceRequestDataObj);
    }

}
