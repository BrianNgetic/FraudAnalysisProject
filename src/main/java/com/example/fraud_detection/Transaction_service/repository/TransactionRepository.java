package com.example.fraud_detection.Transaction_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.fraud_detection.Transaction_service.entity.Transaction;

import java.util.List;

@Repository
public interface  TransactionRepository  extends JpaRepository<Transaction, Long>{
    
    List<Transaction> findByCustomerId(Long id);
    List<Transaction> findTop100ByCustomerIdOrderByTimestampDesc(Long customerId);



}
