package com.mathesis.loadbalancer.controller.balancing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BalancingParamsTransmissionModel {
    private BalancingMethodTypeEnum methodType;
    private RequestCallTypeEnum callType;
    private List<DefaultAppRequestTime> defaultAppsRequestTime;

}
