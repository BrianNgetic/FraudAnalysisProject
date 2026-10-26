package com.example.fraud_detection.fraud_detection_service.publisher;

import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.Transaction_service.repository.*;

@Slf4j
@Component
public class FraudEventPublisher {

    public static final String TOPIC = "fraudEvent-topic";

    private final KafkaTemplate<String, FraudEvent> kafkaTemplate;

    TransactionRepository transactionRepository;

    public FraudEventPublisher(
            @Qualifier("kafkaFraudTemplate") KafkaTemplate<String, FraudEvent> kafkaTemplate,
            TransactionRepository transactionRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void PublishEvent(FraudAnalysisResult result) {

        // create a fraudEvent
        FraudEvent fraudEvent = new FraudEvent(
                UUID.randomUUID(),
                result.getTransaction_id(),
                result.getId(),
                result.getRiskLevel().name(),
                Instant.now());
        // publish
        kafkaTemplate.send(TOPIC, fraudEvent)
                .whenComplete((Result, ex) -> {
                    if (ex == null) {
                        log.info("message sent to topic : {}", fraudEvent);
                    } else {
                        log.error("failed to send message", ex);
                    }
                });

    }

}
