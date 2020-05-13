package com.mathesis.servicetwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServicetwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicetwoApplication.class, args);
	}

}
