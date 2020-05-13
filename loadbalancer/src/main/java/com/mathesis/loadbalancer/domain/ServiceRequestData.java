package com.mathesis.loadbalancer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ServiceRequestData {
    private Instant start;
    private String serviceIdentity;
    private Instant finish;
    private long duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequestData that = (ServiceRequestData) o;
        return duration == that.duration &&
                Objects.equals(start, that.start) &&
                Objects.equals(serviceIdentity, that.serviceIdentity) &&
                Objects.equals(finish, that.finish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceIdentity, duration);
    }
}
