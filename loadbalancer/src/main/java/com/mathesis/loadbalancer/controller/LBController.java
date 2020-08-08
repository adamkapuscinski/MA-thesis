package com.mathesis.loadbalancer.controller;

import com.mathesis.loadbalancer.controller.balancing.model.BalancingParamsTransmissionModel;
import com.mathesis.loadbalancer.domain.TransmissionModel;
import com.mathesis.loadbalancer.domain.chart.ChartSetDataModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class LBController {
    private final LBProvider lbProvider;

    LBController(LBProvider lbProvider) {
        this.lbProvider = lbProvider;
    }

    @GetMapping("api/call")
    public ResponseEntity<Object> call() {
        return ResponseEntity.ok().body(new TransmissionModel(callSomeService()));
    }
    @GetMapping
    public String callSomeService() {
        return lbProvider.callSomeService();
    }

    @GetMapping("api/stats")
    public ChartSetDataModel getStatistics() {
        return lbProvider.assignDataObject();
    }

    @PostMapping("api/assign-params")
    public ResponseEntity<BalancingParamsTransmissionModel> assignBalancingParams(@RequestBody BalancingParamsTransmissionModel balancingParamsTransmissionModel) {
        return ResponseEntity.ok().body(lbProvider.assignBalancingParams(balancingParamsTransmissionModel));
    }
}
