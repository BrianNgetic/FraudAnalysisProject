package com.example.fraud_detection.fraud_detection_service.embedding;

import com.example.fraud_detection.Transaction_service.controller.TransactionController;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.IntStream;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;
import com.example.fraud_detection.fraud_detection_service.repository.EmbeddingRepository;

import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;



@Service
public class EmbeddingService {


    private final EmbeddingModel embeddingModel;
    private final EmbeddingRepository embeddingRepository;


    public EmbeddingService(EmbeddingModel embeddingModel,
                            EmbeddingRepository embeddingRepository
                           ){
        this.embeddingModel = embeddingModel;
        this.embeddingRepository = embeddingRepository;
       
    }


    //we are  not going to embedd the whole result, but rather the relevant pieces
    //we are going to create and store transactionEmbeddings
  
    @Transactional
    public TransactionEmbedding embedTransaction(KafkaTransactionCreatedEvent event,
                                        FraudAnalysisResult result){
                    

            System.out.print("this result has history: " + result.HasHistory());
            //split data to intristic and behavioral


            StringBuilder text = new StringBuilder();

            //instristic
            text.append(String.format(
                "The total Transaction amount was %s .", 
                event.getAmount())

            );
            text.append(String.format(
                "The transaction Occured in %s .",
                event.getCountry())

            );
            text.append(String.format(
                "The transaction happened at %s which was categorized as %s .",
                event.getMerchant(), event.getMerchantCategory())
            );


            if(result.HasHistory()){

                text.append(String.format(
                    "The transaction was deemed %s .",
                    result.getPossibleTravel() ?   "possible:" : "impossible" 
                ));

                text.append(String.format(
                    "The transaction was %s in a new country", 
                    result.getIsNewCountry() ? "made" : "not made "
                ));

                text.append(String.format(
                    "The device used in this transaction was %s .",
                    result.getIsNewDevice() ? "was" : "was not"
                ));
                    
                text.append(String.format(
                    "The Customer has made %s transactions over the past hour .",
                    result.getVelocity()   
                ));


}


                        //part of the document that isnt going to be embedded(metadata)
                Map<String, Object> metadata = Map.of(
                            "transactionId", event.getTransactionId(),
                            "customerId", event.getCustomerId()
                );

            Document document = new Document(text.toString(), metadata);

            float[] embedding = embeddingModel.embed(document.getText());

                TransactionEmbedding transactionEmbedding = new TransactionEmbedding();

                transactionEmbedding.setAnalysisResult(result);
                transactionEmbedding.setCustomerId(event.getCustomerId());
                transactionEmbedding.setEmbedding(embedding);
                transactionEmbedding.setCreatedAt(LocalDateTime.now());
              

              return transactionEmbedding;


}


   

}



