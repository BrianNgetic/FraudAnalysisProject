package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerCountryFrequency {
    

    private Long Id;

    private String Country;

    //how many times a transaction has been from this country
    private long transactionCount;

    @ManyToOne
    private CustomerProfile customerProfile;

    private LocalDateTime lastUpdated;
    
    public void incrementTransactionCount() {
    this.transactionCount++;
    }

    
}
