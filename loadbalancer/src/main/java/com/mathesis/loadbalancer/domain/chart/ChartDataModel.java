package com.mathesis.loadbalancer.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartDataModel {
    private List<String> labels;
    private List<DataSet> datasets;
}


