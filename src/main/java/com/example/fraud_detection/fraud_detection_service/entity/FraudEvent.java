package com.example.fraud_detection.fraud_detection_service.entity;

import java.util.UUID;
import java.time.Instant;

public record FraudEvent (
    UUID eventId,
    Long transactionId,
    Long fraudAnalysisResultId,
    String riskLevel,
    Instant occuredAt
){}