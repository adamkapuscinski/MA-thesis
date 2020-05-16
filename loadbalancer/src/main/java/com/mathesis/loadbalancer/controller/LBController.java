package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import com.mathesis.loadbalancer.domain.chart.ChartDataModel;
import com.mathesis.loadbalancer.domain.chart.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
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

    private ChartDataModel data = new ChartDataModel();

    List<ServiceRequestData> recentRequestData = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostConstruct
    public void init() {
        this.data.setLabels(new ArrayList<>());
        List<DataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < registeredServices.length; i++) {
            DataSet dataSet = new DataSet();
            dataSet.setBorderColor(assignBorderColorBasedOnIdx(i));
            dataSet.setFill(true);
            dataSet.setLabel(registeredServices[i]);
            dataSet.setData(new ArrayList<>());
            dataSets.add(dataSet);
        }
        this.data.setDatasets(dataSets);
    }
    private static String assignBorderColorBasedOnIdx(int idx) {
        switch (idx) {
            case 0: {
                return "#4bc0c0";
            }
            case 1: {
                return "#565656";
            }
            default:
                return "Red";
        }
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

    private void assignParamsToMap(Instant start, Instant finish, String registeredService) {
        long duration = Duration.between(start, finish).toMillis();
        ServiceRequestData serviceRequestDataObj = new ServiceRequestData(start, registeredService, finish, duration);
        assignObjBasedOnRegisteredService(registeredService, serviceRequestDataObj);
    }

    private void assignObjBasedOnRegisteredService(String registeredService, ServiceRequestData serviceRequestDataObj) {
        recentRequestData.add(serviceRequestDataObj);
        List<String> labels = data.getLabels();
        labels.add(serviceRequestDataObj.getStart().toString());

        Optional<DataSet> any = data.getDatasets().stream().filter(o -> o.getLabel().equals(serviceRequestDataObj.getServiceIdentity())).findAny();

        any.map(o -> {
            List<Long> data = o.getData();
            data.add(serviceRequestDataObj.getDuration());
            return o;
        });

        this.map.put(registeredService, recentRequestData.stream().filter(o -> o.getServiceIdentity().equals(registeredService)).collect(Collectors.toList()));
    }

//    @GetMapping("api/stats")
//    public List<ServiceRequestData> getStatistics() {
//        return recentRequestData;
//    }
    @GetMapping("api/stats")
    public ChartDataModel getStatistics() {
        return data;
    }
}
