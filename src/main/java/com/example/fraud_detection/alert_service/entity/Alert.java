package com.example.fraud_detection.alert_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import groovy.transform.builder.Builder;

@Entity 
@Getter 
@Setter 
@AllArgsConstructor 

public class Alert {

    public enum AlertSeverity{
        LOW, 
        MEDIUM,
        HIGH,
        CRITICAL
    }
    public enum AlertStatus{
        NEW,
        ACKNOWLEDGED,
        RESOLVED
    }



    @Id 
    private Long alertId;

    @Column 
    private Long transactionId;

    @Column 
    private Long fraudAnalysisResultId;

    @Column 
    private Long FraudEventId;

    @Column 
    private AlertStatus status;

    @Column 
    private AlertSeverity severity;

    @Column 
    private String title;

    @Column 
    private Instant createdAt;

    @Column 
    private Instant updatedAt;

    @Column 
    private Instant resolvedAt;


    



    

}
