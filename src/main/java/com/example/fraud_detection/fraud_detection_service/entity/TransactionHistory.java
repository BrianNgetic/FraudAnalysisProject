package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class TransactionHistory {
        //this is what is saved, basically all transactions evere

       @Id
         @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;

        @Column private long customerId;

        @Column private  String category;

        @Column private String country;

        @Column private Double latitude;
        
        @Column private Double longitude;

        @Column private double amount;

        @Column private String device;

        @Column private LocalDateTime timestamp;


}
