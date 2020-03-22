package com.mathesis.serviceone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceoneApplication.class, args);
	}

}
