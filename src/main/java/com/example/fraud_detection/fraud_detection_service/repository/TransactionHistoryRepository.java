package com.example.fraud_detection.fraud_detection_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionHistory;

public interface TransactionHistoryRepository  extends JpaRepository<TransactionHistory, Long>{


    List<TransactionHistory> findTop100ByCustomerIdOrderByTimestampDesc(Long customerId);

}
