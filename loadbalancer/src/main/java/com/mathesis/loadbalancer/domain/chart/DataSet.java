package com.mathesis.loadbalancer.domain.chart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSet {
    private String label;
    private List<Long> data;
    private Boolean fill;
    private String borderColor;
}
