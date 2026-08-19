package com.example.fraud_detection.fraud_detection_service.entity;


import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;

public record SimilarTransactionDTO(
     TransactionEmbedding transactionEmbedding,
    double distance,
    double similarity
) {}
