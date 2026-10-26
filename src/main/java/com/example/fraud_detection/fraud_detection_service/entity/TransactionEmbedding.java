package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.annotation.Nonnull;
import jakarta.persistence.ColumnResult;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "transaction_embeddings")

@SqlResultSetMapping(
        name = "SimilarTransactionMapping",
        entities = {
                @EntityResult(
                        entityClass = TransactionEmbedding.class
                )
        },
        columns = {
                @ColumnResult(
                        name = "distance",
                        type = Double.class
                ),
                @ColumnResult(
                        name = "similarity",
                        type = Double.class
                )
        }
)
public class TransactionEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //because OneToOne is optional by default, i have to explicitly 
    //change that
    @OneToOne(fetch = FetchType.EAGER,optional =  false)
    @JoinColumn(name = "Result_Id" ,nullable = false)
    private  FraudAnalysisResult analysisResult;

    private long customerId;

    // @Column(name = "embedding", columnDefinition = "vector(1536")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length  = 1536)
    private float[] embedding;

    private LocalDateTime createdAt;

    public void setEmbedding(float[] embedding){
        this.embedding = embedding;
    }
   
    public Long tempClass(){
         return this.analysisResult.getId();
    }
    
}
