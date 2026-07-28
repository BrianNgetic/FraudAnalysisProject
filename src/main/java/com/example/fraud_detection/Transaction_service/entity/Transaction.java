package com.example.fraud_detection.Transaction_service.entity;


import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Currency;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Country;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.MerchantCategory;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.TransactionStatus;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionId", nullable =  false, updatable = false)
    private long TransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Column
    private Double amount;

    @Column
    private Currency currency;

    @Column 
    private String merchant;
    
    @Column
    private MerchantCategory merchantCategory;

     @Column
    private Country country;

    @Column private Double longitude;

    @Column private Double latitude;

    
    @Column
    private String deviceId;

    @Column 
    private TransactionStatus transactionStatus;

    @Column
    LocalDateTime timestamp = LocalDateTime.now();

    






}
