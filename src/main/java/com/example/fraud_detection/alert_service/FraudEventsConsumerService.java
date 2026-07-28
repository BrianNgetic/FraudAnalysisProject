package com.example.fraud_detection.alert_service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

// import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.kafka.KafkaFraudCreatedEvent;

@Service
public class FraudEventsConsumerService {
    
    private static final String TOPIC = "fraudEvent-topic";

    @KafkaListener(topics = TOPIC, groupId = "alert-service-group", containerFactory = "kafkaFraudListenerContainerFactory")
    public void listenToTopic(KafkaFraudCreatedEvent event){ 
        System.out.println("Recieved Fraud event from Transactionid " + event.transactionId() + "Customer "+ event.consumerId());
    }
}
