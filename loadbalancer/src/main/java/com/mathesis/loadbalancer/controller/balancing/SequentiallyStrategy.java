package com.mathesis.loadbalancer.controller.balancing;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SequentiallyStrategy extends BalancingStrategy {
    private int serviceIdx = 0;

    private DiscoveryClient discoveryClient;

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public ServiceInstance pickService(String[] registeredServices, List<ServiceRequestData> recentServiceRequestData) {
        List<ServiceInstance> instances = this.discoveryClient.getInstances(registeredServices[serviceIdx]);
        serviceIdx++;
        if (serviceIdx > (registeredServices.length - 1)) {
            serviceIdx = 0;
        }
        if (instances.size() > 0) {
            return instances.get(0);
        } else {
            return null;
        }
    }
}
