package com.example.fraud_detection.fraud_detection_service.consumer;

import com.example.fraud_detection.FraudDetectionApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.fraud_detection_service.embedding.EmbeddingService;
import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.service.FraudDetectionAnalyzerService;
import com.example.fraud_detection.fraud_detection_service.repository.FraudAnalysisResultRepository;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.STATUS;
import com.example.fraud_detection.fraud_detection_service.kafka.KafkaFraudEventsPublisher;

@Service
public class TransactionEventsConsumerService {

     private final FraudDetectionApplication fraudDetectionApplication;

     private final KafkaFraudEventsPublisher kafkaFraudEventsPublisher;

     private static final String TOPIC = "transactionEvent-topic";

     private FraudDetectionAnalyzerService fraudDetectionAnalyzerService;
     private EmbeddingService embeddingService;
     private FraudAnalysisResultRepository fraudAnalysisResultRepository;


     public TransactionEventsConsumerService(FraudDetectionAnalyzerService fraudDetectionAnalyzerService,
                                          FraudAnalysisResultRepository fraudAnalysisResultRepository,
                                          KafkaFraudEventsPublisher kafkaFraudEventsPublisher, 
                                          FraudDetectionApplication fraudDetectionApplication,
                                          EmbeddingService embeddingService){
        this.fraudDetectionAnalyzerService = fraudDetectionAnalyzerService;
        this.fraudAnalysisResultRepository = fraudAnalysisResultRepository;
        this.kafkaFraudEventsPublisher = kafkaFraudEventsPublisher;
        this.fraudDetectionApplication = fraudDetectionApplication;
        this.embeddingService = embeddingService;
     }
    
    @KafkaListener(topics = TOPIC , groupId = "fraud-detection-group", containerFactory = "kafkaTransactionListenerContainerFactory") 
    public void  listenToTopic(KafkaTransactionCreatedEvent createdEvent) throws Exception{ 
            System.out.print("Recieved event from transaction ID: " + createdEvent.getTransactionId() + " Customer : "+  createdEvent.getCustomerId());

        FraudAnalysisResult analysis_result = fraudDetectionAnalyzerService.isFraud(createdEvent);

        fraudAnalysisResultRepository.save(analysis_result);
       
        //embedds and saves 
        embeddingService.embedTransaction(createdEvent, analysis_result);


        //if it isnt fraud then lets just save the event
        if(analysis_result.getStatus() == STATUS.NOT_FRAUD){
                fraudAnalysisResultRepository.save(analysis_result);
        }
        else if(analysis_result.getStatus() == STATUS.FRAUD){
                kafkaFraudEventsPublisher.PublishEvent(analysis_result);
        }



        //if it is we publish it to the fraud_events service
        }
 

}

