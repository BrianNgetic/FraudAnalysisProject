package com.example.fraud_detection.fraud_detection_service.kafka;

import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.Transaction_service.repository.*;

@Slf4j
@Component
public class KafkaFraudEventsPublisher {

    
    public static final String TOPIC = "fraudEvent-topic";

    private final KafkaTemplate<String, KafkaFraudCreatedEvent> kafkaTemplate;

    TransactionRepository transactionRepository;

    public KafkaFraudEventsPublisher(
        @Qualifier("kafkaFraudTemplate")
        KafkaTemplate<String,KafkaFraudCreatedEvent> kafkaTemplate,
        TransactionRepository transactionRepository)
        {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionRepository = transactionRepository;
        }

    public void PublishEvent(FraudAnalysisResult result){

        //get the transaction so that we can get multiple fields related to it
       Transaction thisTransaction =  transactionRepository.getReferenceById(result.getTransaction_id());

        //create a fraudEvent
            // KafkaFraudCreatedEvent fraudEvent = new KafkaFraudCreatedEvent(
            //         UUID.randomUUID().toString(),
            //         result.getTransaction_id(),
            //         result.getCustomer_id(),
            //         thisTransaction.getAmount(),
            //         thisTransaction.getCurrency().toString(),
            //         thisTransaction.getMerchantCategory().toString(),
            //         thisTransaction.getCountry().toString(),
            //         result.getRiskScore(),
            //         result.getStatus().toString(),
            //         result.getReasons(),
            //         result.getAnalyzed_at()
            // );
        //publish
        // kafkaTemplate.send(TOPIC, fraudEvent)
        //                 .whenComplete( (Result, ex) -> {
        //                 if(ex == null){
        //                     log.info("message sent to topic : {}", fraudEvent );
        //                 }
        //                 else{
        //                     log.error("failed to send message", ex);
        //                 }
        //             });

    }
    
}
