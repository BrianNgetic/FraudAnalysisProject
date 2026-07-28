package com.example.fraud_detection.fraud_detection_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;

@Repository
public interface FraudAnalysisResultRepository  extends JpaRepository<FraudAnalysisResult, Long>{
    
}
