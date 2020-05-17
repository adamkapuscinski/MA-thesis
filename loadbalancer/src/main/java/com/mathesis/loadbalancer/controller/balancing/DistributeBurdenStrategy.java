package com.mathesis.loadbalancer.controller.balancing;

import com.mathesis.loadbalancer.domain.Pair;
import com.mathesis.loadbalancer.domain.ServiceRequestData;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DistributeBurdenStrategy extends BalancingStrategy {

    private DiscoveryClient discoveryClient;

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public ServiceInstance pickService(String[] registeredServices, List<ServiceRequestData> recentServiceRequestData) {
        String instanceIdentity = "";

        Map<String, List<Long>> collect = recentServiceRequestData.stream().filter(o -> o.getFinish().isAfter(Instant.now().minus(30, ChronoUnit.SECONDS)))
                .collect(Collectors.groupingBy(ServiceRequestData::getServiceIdentity,
                        Collectors.mapping(ServiceRequestData::getDuration,
                                Collectors.toList()
                        )
                ));
        if (collect.size() == 0 && registeredServices.length > 0) {
            instanceIdentity = registeredServices[0];
        } else if (collect.size() < registeredServices.length) {
            Optional<String> first = Arrays.stream(registeredServices).filter(service -> !collect.containsKey(service)).findFirst();
            if (first.isPresent()) {
                return getServiceInstance(first.get());
            }
        }

        Pair<String, Double> minValuePair = new Pair<>();

        collect.forEach((key, value) -> {
            double v = value.stream().mapToLong(o -> o).average().orElse(0) * value.size();

            if (Objects.isNull(minValuePair.getValue())) {
                minValuePair.setKey(key);
                minValuePair.setValue(v);
            } else if (minValuePair.getValue() > v) {
                minValuePair.setKey(key);
                minValuePair.setValue(v);
            }
        });
        return getServiceInstance(instanceIdentity.equals("") ? minValuePair.getKey() : instanceIdentity);
    }
    private ServiceInstance getServiceInstance(String instanceIdentity) {
        if (Objects.nonNull(instanceIdentity) && !instanceIdentity.equals("")) {
            List<ServiceInstance> instances = this.discoveryClient.getInstances(instanceIdentity);
            if (instances.size() > 0) {
                return instances.get(0);
            }
        }
        return null;
    }
}
