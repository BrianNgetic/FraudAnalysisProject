package com.example.fraud_detection.fraud_detection_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;


public interface FraudAnalysisResultRepository  extends JpaRepository<FraudAnalysisResult, Long>{
    
}
