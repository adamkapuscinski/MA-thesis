package com.mathesis.loadbalancer.controller.balancing.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class DefaultAppRequestTime {
    private String applicationType;
    private Integer requestTime;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DefaultAppRequestTime that = (DefaultAppRequestTime) object;
        return Objects.equals(applicationType, that.applicationType) &&
                Objects.equals(requestTime, that.requestTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationType, requestTime);
    }
}
