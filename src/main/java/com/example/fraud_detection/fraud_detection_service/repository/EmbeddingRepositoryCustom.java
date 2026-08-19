package com.example.fraud_detection.fraud_detection_service.repository;


import java.util.List;
import com.example.fraud_detection.fraud_detection_service.entity.SimilarTransactionDTO;




public interface EmbeddingRepositoryCustom {
    
    List<SimilarTransactionDTO> findSimilar(
            float[] embedding,
            int limit
    );

} 

