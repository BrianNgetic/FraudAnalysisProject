package com.example.fraud_detection.alert_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud_detection.alert_service.entity.Alert;


public interface AlertRepository extends JpaRepository<Alert , Long> {

    
} 
