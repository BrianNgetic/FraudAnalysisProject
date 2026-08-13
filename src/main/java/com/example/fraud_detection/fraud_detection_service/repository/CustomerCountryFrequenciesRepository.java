package com.example.fraud_detection.fraud_detection_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud_detection.fraud_detection_service.entity.CustomerCountryFrequency;
public interface CustomerCountryFrequenciesRepository  extends JpaRepository<CustomerCountryFrequency, Long>{

    
} 
