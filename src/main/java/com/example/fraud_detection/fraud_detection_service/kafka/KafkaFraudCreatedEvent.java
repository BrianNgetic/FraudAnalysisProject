package com.example.fraud_detection.fraud_detection_service.kafka;


import java.time.LocalDateTime;
import java.util.List;

public record KafkaFraudCreatedEvent (

    String eventId,
    Long transactionId,
    Long consumerId,
    Double amount,
    String currency,
    String merchantCategory,
    String country,
    Double riskScore,
    String status,
    List<String> reasons,
    LocalDateTime detectedAt
    
){}