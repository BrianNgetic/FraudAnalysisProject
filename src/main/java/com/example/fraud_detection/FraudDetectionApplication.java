package com.example.fraud_detection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
// (scanBasePackages = "com.example.fraud_detection")
// @EntityScan(basePackages = "com.example.fraud_detection.Transaction_service.entity")
// @EnableJpaRepositories(basePackages = "com.example.fraud_detection.Transaction_service.repository")
public class FraudDetectionApplication{

	public static void main(String[] args) {
		SpringApplication.run(FraudDetectionApplication.class, args);
	}



}
