package com.mathesis.loadbalancer.controller.balancing;

import com.mathesis.loadbalancer.domain.ServiceRequestData;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

public abstract class BalancingStrategy {
    public abstract ServiceInstance pickService(String[] registeredServices, List<ServiceRequestData> recentServiceRequestData);
    public abstract void setDiscoveryClient(DiscoveryClient discoveryClient);
}
