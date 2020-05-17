package com.mathesis.loadbalancer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<T, P> {
    private T key;
    private P value;
}
