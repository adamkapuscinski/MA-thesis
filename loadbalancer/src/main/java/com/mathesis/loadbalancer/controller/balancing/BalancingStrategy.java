package com.mathesis.loadbalancer.controller.balancing;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

public abstract class BalancingStrategy {
    public abstract ServiceInstance pickService(String[] registeredServices, List<ServiceRequestData> recentServiceRequestData);
    public abstract void setDiscoveryClient(DiscoveryClient discoveryClient);
    public ServiceInstance pickServiceBasedOnName(DiscoveryClient discoveryClient, String appName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(appName);
        if (instances.size() > 0) {
            return instances.get(0);
        } else {
            return null;
        }
    }
}
