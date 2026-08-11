package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Setter;

@Entity
@Setter
@Table(name = "transaction_embeddings")
public class TransactionEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;

    private long customerId;

    // @Column(name = "embedding", columnDefinition = "vector(1536")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length  = 1536)
    private float[] embedding;

    private Instant createdAt;

    public void setEmbedding(float[] embedding){
        this.embedding = embedding;
    }
   
    
}
