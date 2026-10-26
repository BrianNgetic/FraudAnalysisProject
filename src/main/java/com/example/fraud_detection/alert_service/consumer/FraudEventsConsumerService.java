package com.example.fraud_detection.alert_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;
import com.example.fraud_detection.alert_service.entity.Alert;

@Service
public class FraudEventsConsumerService {

    private static final String TOPIC = "fraudEvent-topic";

    @KafkaListener(topics = TOPIC, groupId = "alert-service-group", containerFactory = "kafkaFraudListenerContainerFactory")
    public void listenToTopic(FraudEvent event) {
        System.out.println(
                "Recieved Fraud event from Transactionid " + event.transactionId());
        System.out.println("EUREKA");


        //create an alert of of this event
        // Alert newAlert = new Alert(

        // );  
    }
}
