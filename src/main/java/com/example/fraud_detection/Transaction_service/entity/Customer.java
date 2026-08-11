package com.example.fraud_detection.Transaction_service.entity;

import java.time.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customerId", nullable = false, updatable = false)
   private Long Id;
   
    @Column(length = 20)
   private String name;

    @Column(length = 20, unique = true)
    private String email;

    @Column
    private LocalDateTime createdAt;
}
