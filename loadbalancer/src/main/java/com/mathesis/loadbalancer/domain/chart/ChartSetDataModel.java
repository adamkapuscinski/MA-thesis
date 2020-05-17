package com.mathesis.loadbalancer.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartSetDataModel {
    private ChartDataModel firstSet;
    private ChartDataModel secondSet;
}
