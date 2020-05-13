package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class LBController {
    private String[] registeredServices = new String[]{"SERVICEONE", "SERVICETWO", "SERVICETHREE"};
    private int serviceIdx = 0;

    private Map<String, List<ServiceRequestData>> map = new HashMap<>();

    List<ServiceRequestData> recentRequestData = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

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

    private void assignParamsToMap(Instant start, Instant finish, String registeredService) {
        long duration = Duration.between(start, finish).toNanos();
        ServiceRequestData serviceRequestDataObj = new ServiceRequestData(start, registeredService, finish, duration);
        assignObjBasedOnRegisteredService(registeredService, serviceRequestDataObj);
    }

    private void assignObjBasedOnRegisteredService(String registeredService, ServiceRequestData serviceRequestDataObj) {
        recentRequestData.add(serviceRequestDataObj);
        this.map.put(registeredService, recentRequestData.stream().filter(o -> o.getServiceIdentity().equals(registeredService)).collect(Collectors.toList()));
    }

    @GetMapping("api/stats")
    public List<ServiceRequestData> getStatistics() {
        return recentRequestData;
    }
}
