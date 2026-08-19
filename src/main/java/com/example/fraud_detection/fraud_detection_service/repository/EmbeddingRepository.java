package com.example.fraud_detection.fraud_detection_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;

@Repository
public interface EmbeddingRepository  extends JpaRepository<TransactionEmbedding, Long>,   EmbeddingRepositoryCustom{

         
}
    
