package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import com.mathesis.loadbalancer.domain.TransmissionModel;
import com.mathesis.loadbalancer.domain.chart.ChartDataModel;
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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("")
public class LBController {
    private String[] registeredServices = new String[]{"SERVICEONE", "SERVICETWO", "SERVICETHREE"};
    private int serviceIdx = 0;

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
    public ChartDataModel getStatistics() {
        return assignDataObject();
    }

    private ChartDataModel assignDataObject() {
        int timeToShowInMinutes = 5;
        int synchronizationTime = 20;

        ChartDataModel result = new ChartDataModel();

        Instant now = Instant.now();
        Instant before = now.minus(timeToShowInMinutes, ChronoUnit.MINUTES);

        List<String> labels = new ArrayList<>();
        List<DataSet> dataSets = new ArrayList<>();

        for (int i = 0; i < registeredServices.length; i++) {
            DataSet dataSet = new DataSet(registeredServices[i], new ArrayList<>(), true, assignBorderColorBasedOnIdx(i));
            int finalI = i;

            Predicate<ServiceRequestData> registeredServicePredicate = requestData -> requestData.getServiceIdentity().equals(registeredServices[finalI]);
            List<ServiceRequestData> serviceRequestDataStream = recentRequestData.stream().filter(registeredServicePredicate).collect(Collectors.toList());

            List<Long> data = new ArrayList<>();

            do {
                if (i == 0)
                    labels.add(before.toString());

                Instant finalBefore = before;
                Instant finalCeilBefore = finalBefore.plus(synchronizationTime, ChronoUnit.SECONDS);

                Predicate<ServiceRequestData> datePredicate = requestData ->
                        (requestData.getStart().isAfter(finalBefore) && requestData.getStart().isBefore(finalCeilBefore)) || requestData.getStart().equals(finalBefore) || requestData.getStart().equals(finalCeilBefore);

                double v = serviceRequestDataStream.stream().filter(datePredicate).map(ServiceRequestData::getDuration)
                            .mapToLong(o -> o).average().orElse(0);

                data.add(Math.round(v));

                before = before.plus(synchronizationTime, ChronoUnit.SECONDS);
            } while (before.plus(synchronizationTime, ChronoUnit.SECONDS).isBefore(now));
            before = now.minus(timeToShowInMinutes, ChronoUnit.MINUTES);
            dataSet.setData(data);
            dataSets.add(dataSet);
        }

        result.setDatasets(dataSets);
        result.setLabels(labels);
        return result;
    }

    private void assignParamsToMap(Instant start, Instant finish, String registeredService) {
        long duration = Duration.between(start, finish).toMillis();
        ServiceRequestData serviceRequestDataObj = new ServiceRequestData(start, registeredService, finish, duration);
        recentRequestData.add(serviceRequestDataObj);
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
}
