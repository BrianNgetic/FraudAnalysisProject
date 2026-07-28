package com.example.demo;

import org.springframework.boot.SpringApplication;

import com.example.fraud_detection.FraudDetectionApplication;

public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(FraudDetectionApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
