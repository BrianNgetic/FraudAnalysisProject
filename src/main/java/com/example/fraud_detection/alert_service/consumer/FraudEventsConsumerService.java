package com.example.fraud_detection.alert_service.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.alert_service.service.AlertService;

import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;
import com.example.fraud_detection.alert_service.entity.Alert;

@Service
public class FraudEventsConsumerService {

    private final AlertService alertService;
    private static final String TOPIC = "fraudEvent-topic";

     public   FraudEventsConsumerService(AlertService alertService) {
        this.alertService = alertService;
    }

  

    @KafkaListener(topics = TOPIC, groupId = "alert-service-group", containerFactory = "kafkaFraudListenerContainerFactory")
    public void listenToTopic(FraudEvent event) throws Exception {
        System.out.println(
                "Recieved Fraud event from Transactionid " + event.transactionId());
        System.out.println("EUREKA");


       Alert  newAlert = alertService.createAlert(event);
    
    //create an alert of of th

       
    }

}
