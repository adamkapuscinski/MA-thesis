package com.mathesis.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingBean {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("**")
                        .filters(f -> f.rewritePath("(?<remains>.*)", "${remains}")
                        .addRequestHeader("x-first-Header", "first-service-header")
                            .hystrix(c -> c.setName("hystrix")
                            .setFallbackUri("forward:/fallback/first")))
                        .uri("lb://SERVICEONE/")
                        .id("serviceone"))
            .build();
    }
}
