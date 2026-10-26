package com.example.fraud_detection.Transaction_service.kafka;



import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Component;

import java.time.*;

import lombok.extern.slf4j.Slf4j;

import com.example.fraud_detection.Transaction_service.entity.Transaction;
@Slf4j
@Component
public class KafkaTransactionEventsPublisher {
    //this is the producer
    
    private static final String TOPIC = "transactionEvent-topic";


      private  final KafkaTemplate<String, KafkaTransactionCreatedEvent> kafkaTemplate;

      public KafkaTransactionEventsPublisher(
      @Qualifier("kafkaTransactionTemplate")  
      KafkaTemplate<String, KafkaTransactionCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
      }



      public void publishTransaction(Transaction savedTransaction) throws Exception{
        //create an TransactionEvent from the transaction, it includes the customers id

        System.out.println("about to try to pass in timestamp :" + savedTransaction.getTimestamp());
        //create a valid kafka event from the transaction 
          KafkaTransactionCreatedEvent transactionCreatedEvent = KafkaTransactionCreatedEvent.builder()
                            .transactionId(savedTransaction.getTransactionId())
                            .customerId(savedTransaction.getCustomer().getId())
                            .amount(savedTransaction.getAmount())
                            .currency(savedTransaction.getCurrency().toString())
                            .country(savedTransaction.getCountry().toString())
                            .latitude(savedTransaction.getLatitude())
                            .longitude(savedTransaction.getLongitude())
                            .merchant(savedTransaction.getMerchant())
                            .merchantCategory(savedTransaction.getMerchantCategory().toString())
                            .device(savedTransaction.getDeviceId())
                            .timestamp(savedTransaction.getTimestamp())
                            .build();

        //publish and log it
         kafkaTemplate.send(TOPIC, transactionCreatedEvent)
                    .whenComplete( (result, ex) -> {
                        if(ex == null){
                            log.info("message sent to topic : {}", transactionCreatedEvent );
                        }
                        else{
                            log.error("failed to send message", ex);
                        }
                    });


      }

    
}
