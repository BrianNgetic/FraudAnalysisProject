package com.example.fraud_detection.fraud_detection_service.dto;

public record FraudAnalysisResultRequestDto(
    Long Id,
    Long customer_id,
    String riskLevel,
    String deviceId,
    TransactionRequestDto transactionRequestDto,
    double longitude,
    double latitude
) {} 
//need some more things from the transaction
//like 