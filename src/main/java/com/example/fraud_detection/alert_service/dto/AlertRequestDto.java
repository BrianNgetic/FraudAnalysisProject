package com.example.fraud_detection.alert_service.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Builder 
public class AlertRequestDto {
    
    private Long transactionId;

    private Long fraudAnalysisResultId;
    
    
    private UUID fraudEventId;

    
    private String  status;

    
    private String  severity;

    
    private String title;

    
    private Instant createdAt;

    
    private Instant updatedAt;

    
    private Instant resolvedAt;

}
