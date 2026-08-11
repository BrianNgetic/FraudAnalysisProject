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
    private  Long riskScore;

    private Long velocity;

    @Column  private LEVEL riskLevel; 

    @Column private String Country;

    @Column
    private Boolean isNewMerchant;

    @Column
    private Boolean isNewDevice;

    @Column
    private boolean isNewCountry;


    @Column
    private Boolean impossibleTravel;

    

    @Column 
    private STATUS status;

    @Column
     private List<String> reasons;

    @Column
    private  LocalDateTime analyzed_at;

    public void addReason(String reason){
        this.reasons.add(reason);
    }

    public boolean getIsNewCountry(){
        return isNewCountry;
    }

//in the second version of this i want to add time and country 
//usual/unusuallnesss

}
