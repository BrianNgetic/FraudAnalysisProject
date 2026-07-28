package com.example.fraud_detection.fraud_detection_service.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraud_detection.fraud_detection_service.entity.CustomerProfile;
// import com.example.fraud_detection.Transaction_service.entity.Customer;



public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long> {

    Optional<CustomerProfile> findByCustomerId(Long customerId);
} 
