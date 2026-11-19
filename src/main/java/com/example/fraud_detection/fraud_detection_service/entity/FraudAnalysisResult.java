package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.LocalDateTime;
import java.util.List;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "Result_Id")
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
    private Boolean possibleTravel;

    

    @Column 
    // @Enumerated(EnumType.STRING)
    private STATUS status;

    @Column
     private List<String> reasons;

    @Column
    private  LocalDateTime analyzed_at;

    @Transient
    private boolean hasHistory;
    
    public void addReason(String reason){
        this.reasons.add(reason);
    }

    public boolean getIsNewCountry(){
        return isNewCountry;
    }

    public boolean HasHistory(){
        return hasHistory;
    }

     public void setIsNewCountry(Boolean isNewCountry){
        this.isNewCountry = isNewCountry;
    }
//in the second version of this i want to add time and country 
//usual/unusuallnesss

}
