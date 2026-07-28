package com.example.fraud_detection.Transaction_service.kafka;


import java.time.LocalDateTime;

import io.micrometer.common.lang.NonNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
@Getter 
@Jacksonized
@Builder
public class KafkaTransactionCreatedEvent {
    @NonNull
    private long transactionId;
    @NonNull
    private long customerId;
    private double amount;
    private String currency;
    private String country;
    private double longitude;
    private double latitude;
    private String merchant;
    private String merchantCategory;
    private String device;
    private LocalDateTime timestamp;
    
    
}
