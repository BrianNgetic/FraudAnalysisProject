package com.example.fraud_detection.alert_service.consumer;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.alert_service.service.AlertService;

import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;
import com.example.fraud_detection.alert_service.entity.Alert;
import com.example.fraud_detection.alert_service.repository.AlertRepository;

@Service
public class FraudEventsConsumerService {

    private final AlertService alertService;
    private final AlertRepository alertRepository;
    private static final String TOPIC = "fraudEvent-topic";

     public   FraudEventsConsumerService(AlertService alertService, AlertRepository alertRepository) {

        this.alertService = alertService;
        this.alertRepository  = alertRepository;
    }

  

    @KafkaListener(topics = TOPIC, groupId = "alert-service-group", containerFactory = "kafkaFraudListenerContainerFactory")
    public void listenToTopic(FraudEvent event) throws Exception {
        System.out.println(
                "Recieved Fraud event from Transactionid " + event.transactionId());
        System.out.println("EUREKA");


       Alert  newAlert = alertService.createAlert(event);
    
       if(newAlert != null){
          alertRepository.save(newAlert);
       }
 
    
    //create an alert of of th

       
    }

}
