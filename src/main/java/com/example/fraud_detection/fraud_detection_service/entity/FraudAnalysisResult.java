package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.LocalDateTime;
import java.util.List;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
public class FraudAnalysisResult {

    public enum STATUS{
        FRAUD,
        NOT_FRAUD,
        REVIEW
    }

    public enum LEVEL{
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private Long transaction_id;

    @Column
    private Long customer_id;

    @Column
    private Double riskScore;

    @Column  private LEVEL riskLevel; 


    @Column 
    private STATUS status;

    @Column
     private List<String> reasons;

    @Column
    private  LocalDateTime analyzed_at;

    public void addReason(String reason){
        this.reasons.add(reason);
    }



}
