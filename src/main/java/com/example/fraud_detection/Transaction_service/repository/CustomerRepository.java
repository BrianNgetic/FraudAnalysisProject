package com.example.fraud_detection.Transaction_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fraud_detection.Transaction_service.entity.Customer;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer, Long>{
   Boolean existsByemail(String email); 
}
