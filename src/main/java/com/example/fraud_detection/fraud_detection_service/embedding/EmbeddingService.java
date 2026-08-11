package com.example.fraud_detection.fraud_detection_service.embedding;

import com.example.fraud_detection.Transaction_service.controller.TransactionController;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;





import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;
import com.example.fraud_detection.fraud_detection_service.repository.EmbeddingRepository;



@Service
public class EmbeddingService {


    private final EmbeddingModel embeddingModel;
    private final EmbeddingRepository embeddingRepository;

    public EmbeddingService(EmbeddingModel embeddingModel,
                           

                            EmbeddingRepository embeddingRepository){
        this.embeddingModel = embeddingModel;
        this.embeddingRepository = embeddingRepository;
    }

    
    
    //we are  not going to embedd the whole result, but rather the relevant pieces
    //we are going to create and store transactionEmbeddings
    public void embedTransaction(KafkaTransactionCreatedEvent event,
                                FraudAnalysisResult result){
            

    
    
    
     String text = 
                """ 
                The total Transaction  amount was %s.
                The customer made the transaction in %s with travel to make the transaction deemed %s.
                The transaction was categorized as %s.
                The customer %s the transaction in a new country and the device %s new.
                The customer has made %s transactions in the past hour.
                """.formatted(
                            event.getAmount(),
                            event.getCountry(),
                            result.getImpossibleTravel() ? "impossible" : "possible",
                            event.getMerchant(),
                            event.getMerchantCategory(),
                            result.getIsNewCountry() ? "made" : "did not make ",
                            result.getIsNewDevice() ? "was" : "was not",
                            result.getVelocity()
                );

                //part of the document that isnt going to be embedded(metadata)
        Map<String, Object> metadata = Map.of(
                    "transactionId", event.getTransactionId(),
                    "customerId", event.getCustomerId()
          );

       Document document = new Document(text, metadata);

       float[] embedding = embeddingModel.embed(document.getText());

        TransactionEmbedding transactionEmbedding = new TransactionEmbedding();

        transactionEmbedding.setTransactionId(event.getTransactionId());
        transactionEmbedding.setCustomerId(event.getCustomerId());

        transactionEmbedding.setEmbedding(embedding);
        transactionEmbedding.setCreatedAt(Instant.now());

        embeddingRepository.save(transactionEmbedding);



    }
}

