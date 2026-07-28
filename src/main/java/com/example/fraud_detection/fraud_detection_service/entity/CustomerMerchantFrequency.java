package com.example.fraud_detection.fraud_detection_service.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CustomerMerchantFrequency {
    
    @Id
    @GeneratedValue
    private Long Id;

    private String merchant;
    
    //how many  times a transaction has been from the above merchant
    private long transactionCount;

    @ManyToOne
    private CustomerProfile customerProfile;

    private LocalDateTime lastUpdated;
    
    public void incrementTransactionCount() {
        this.transactionCount++;
    }

}
