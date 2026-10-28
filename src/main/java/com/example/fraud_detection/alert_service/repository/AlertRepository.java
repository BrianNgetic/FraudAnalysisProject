package com.example.fraud_detection.alert_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud_detection.alert_service.entity.Alert;


public interface AlertRepository extends JpaRepository<Alert , Long> {

    //this is for the dashboard
 
    Page<Alert> findLatestAlerts(Pageable latestFirst);

 
    
}
