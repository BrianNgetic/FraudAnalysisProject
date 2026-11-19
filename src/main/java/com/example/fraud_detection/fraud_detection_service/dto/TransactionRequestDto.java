package com.example.fraud_detection.fraud_detection_service.dto;

public record TransactionRequestDto(
    Long id,
    Long customerId,
    Double amount,
    String currency, 
    String merchant, 
    String timeStamp
){};
