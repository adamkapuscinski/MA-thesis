package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.controller.balancing.BalancingStrategy;
import com.mathesis.loadbalancer.controller.balancing.DistributeBurdenStrategy;
import com.mathesis.loadbalancer.controller.balancing.SequentiallyStrategy;
import com.mathesis.loadbalancer.controller.balancing.model.BalancingParamsTransmissionModel;
import com.mathesis.loadbalancer.controller.balancing.model.BalancingMethodTypeEnum;
import com.mathesis.loadbalancer.controller.balancing.model.DefaultAppRequestTime;
import com.mathesis.loadbalancer.controller.balancing.model.RequestCallTypeEnum;
import com.mathesis.loadbalancer.domain.ServiceRequestData;
import com.mathesis.loadbalancer.domain.chart.ChartDataModel;
import com.mathesis.loadbalancer.domain.chart.ChartSetDataModel;
import com.mathesis.loadbalancer.domain.chart.DataSet;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class LBProvider {
    private String[] registeredServices = new String[]{"SERVICEONE", "SERVICETWO", "SERVICETHREE"};

    private ChartDataModel data = new ChartDataModel();
    private BalancingParamsTransmissionModel lastBalancingParamsTransmissionModel = new BalancingParamsTransmissionModel(BalancingMethodTypeEnum.AVERAGE_RESPONSE_TIME, RequestCallTypeEnum.ESTABLISHED_PARAMS, new ArrayList<>());
    private List<ServiceRequestData> recentRequestData = new ArrayList<>();

    private RestTemplate restTemplate;
    private DiscoveryClient discoveryClient;
    private BalancingStrategy balancingStrategy;

    public LBProvider(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @PostConstruct
    public void init() {
        balancingStrategy = pickStrategyBasedOnBalancingMethod(lastBalancingParamsTransmissionModel.getMethodType());
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

    ChartSetDataModel assignDataObject() {
        int timeToShowInMinutes = 5;
        int synchronizationTime = 10;

        ChartSetDataModel result = new ChartSetDataModel();
        ChartDataModel firstSet = new ChartDataModel();
        ChartDataModel secondSet = new ChartDataModel();

        Instant now = Instant.now();
        Instant before = now.minus(timeToShowInMinutes, ChronoUnit.MINUTES);

        List<String> labels = new ArrayList<>();
        List<DataSet> firstDataSets = new ArrayList<>();
        List<DataSet> secondDataSets = new ArrayList<>();

        for (int i = 0; i < registeredServices.length; i++) {
            DataSet dataSet = new DataSet(registeredServices[i], new ArrayList<>(), true, assignBorderColorBasedOnIdx(i));
            DataSet secDataSet = new DataSet(registeredServices[i], new ArrayList<>(), true, assignBorderColorBasedOnIdx(i));

            int finalI = i;

            Predicate<ServiceRequestData> registeredServicePredicate = requestData -> requestData.getServiceIdentity().equals(registeredServices[finalI]);
            List<ServiceRequestData> serviceRequestDataStream = recentRequestData.stream().filter(registeredServicePredicate).collect(Collectors.toList());

            List<Long> firstData = new ArrayList<>();
            List<Long> secondData = new ArrayList<>();

            do {
                if (i == 0)
                    labels.add(before.toString());

                Instant finalBefore = before;
                Instant finalCeilBefore = finalBefore.plus(synchronizationTime, ChronoUnit.SECONDS);

                Predicate<ServiceRequestData> datePredicate = requestData ->
                        (requestData.getStart().isAfter(finalBefore) && requestData.getStart().isBefore(finalCeilBefore)) || requestData.getStart().equals(finalBefore) || requestData.getStart().equals(finalCeilBefore);

                double v = serviceRequestDataStream.stream().filter(datePredicate).map(ServiceRequestData::getDuration)
                        .mapToLong(o -> o).average().orElse(0);
                long count = serviceRequestDataStream.stream().filter(datePredicate).count();

                firstData.add(Math.round(v));
                secondData.add(count);

                before = before.plus(synchronizationTime, ChronoUnit.SECONDS);
            } while (before.plus(synchronizationTime, ChronoUnit.SECONDS).isBefore(now));
            before = now.minus(timeToShowInMinutes, ChronoUnit.MINUTES);
            dataSet.setData(firstData);
            firstDataSets.add(dataSet);
            secDataSet.setData(secondData);
            secondDataSets.add(secDataSet);
        }

        firstSet.setDatasets(firstDataSets);
        firstSet.setLabels(labels);
        secondSet.setLabels(labels);
        secondSet.setDatasets(secondDataSets);

        result.setFirstSet(firstSet);
        result.setSecondSet(secondSet);
        return result;
    }

    static String assignBorderColorBasedOnIdx(int idx) {
        switch (idx) {
            case 0: {
                return "#4bc0c0";
            }
            case 1: {
                return "#565656";
            }
            default:
                return "red";
        }
    }
    String callSomeService() {
        ServiceInstance serviceInstance = balancingStrategy.pickService(registeredServices, recentRequestData);

        String urlAddon = "";
        RequestCallTypeEnum callType = lastBalancingParamsTransmissionModel.getCallType();
        if (callType.equals(RequestCallTypeEnum.RANDOM_MATRIX_TRANSPOSE)) {
            urlAddon = "random-burden";
        }
        String result = callGetMethodOnServiceInstance(serviceInstance, urlAddon, true);
        return result;
    }

    private void assignParamsToMap(Instant start, Instant finish, String registeredService) {
        long duration = Duration.between(start, finish).toMillis();
        ServiceRequestData serviceRequestDataObj = new ServiceRequestData(start, registeredService, finish, duration);
        recentRequestData.add(serviceRequestDataObj);
    }

    BalancingParamsTransmissionModel assignBalancingParams(BalancingParamsTransmissionModel balancingParamsTransmissionModel) {
        if (Objects.nonNull(balancingParamsTransmissionModel)) {
            balancingStrategy = pickStrategyBasedOnBalancingMethod(balancingParamsTransmissionModel.getMethodType());
            assignDefaultParameters(balancingParamsTransmissionModel.getDefaultAppsRequestTime());
            balancingStrategy.setDiscoveryClient(discoveryClient);
            recentRequestData = new ArrayList<>();
            lastBalancingParamsTransmissionModel = balancingParamsTransmissionModel;
        }
        return balancingParamsTransmissionModel;
    }

    private void assignDefaultParameters(List<DefaultAppRequestTime> defaultAppsRequestTime) {
        defaultAppsRequestTime.forEach(appRequestTime -> {
            ServiceInstance serviceInstance = balancingStrategy.pickServiceBasedOnName(discoveryClient, appRequestTime.getApplicationType());
            callGetMethodOnServiceInstance(serviceInstance, "param/" + appRequestTime.getRequestTime(), false);
        });
    }
    private String callGetMethodOnServiceInstance(ServiceInstance serviceInstance, String urlAddon, Boolean shouldAddConnectionTimeToMap) {
        String result = "";
        if (Objects.nonNull(serviceInstance)) {
            String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/" + urlAddon;
            System.out.println("calling url: " + url);
            Instant start = Instant.now();
            result = restTemplate.getForObject(url, String.class);
            Instant finish = Instant.now();
            if (shouldAddConnectionTimeToMap)
                assignParamsToMap(start, finish, serviceInstance.getServiceId());
        }
        return result;
    }

    private BalancingStrategy pickStrategyBasedOnBalancingMethod(BalancingMethodTypeEnum balancingMethodTypeEnum) {
        BalancingStrategy res = new DistributeBurdenStrategy();
        if (BalancingMethodTypeEnum.SEQUENTIALLY.equals(balancingMethodTypeEnum)) {
            res = new SequentiallyStrategy();
        } else if (BalancingMethodTypeEnum.AVERAGE_RESPONSE_TIME.equals(balancingMethodTypeEnum)) {
            res = new DistributeBurdenStrategy();
        }
        return res;
    }
}
