package com.example.fraud_detection.fraud_detection_service.consumer;

import com.example.fraud_detection.FraudDetectionApplication;

import com.example.fraud_detection.fraud_detection_service.rules.TravelAnalyzer;
import java.util.List;

import org.springframework.ai.embedding.Embedding;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.fraud_detection_service.embedding.EmbeddingService;
import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.service.FraudDetectionAnalyzerService;

import jakarta.transaction.Transactional;

import com.example.fraud_detection.fraud_detection_service.repository.FraudAnalysisResultRepository;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.entity.SimilarTransactionDTO;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.LEVEL;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.STATUS;
import com.example.fraud_detection.fraud_detection_service.publisher.FraudEventPublisher;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionEmbedding;
import com.example.fraud_detection.fraud_detection_service.repository.EmbeddingRepository;

import com.example.fraud_detection.fraud_detection_service.repository.EmbeddingRepositoryCustomImpl;

@Service
public class TransactionEventsConsumerService {

        private final TravelAnalyzer travelAnalyzer;

        private final FraudDetectionApplication fraudDetectionApplication;

        private final FraudEventPublisher FraudEventPublisher;

        private static final String TOPIC = "transactionEvent-topic";

        private FraudDetectionAnalyzerService fraudDetectionAnalyzerService;
        private EmbeddingService embeddingService;
        private FraudAnalysisResultRepository fraudAnalysisResultRepository;
        private EmbeddingRepositoryCustomImpl customEmbeddingRepository;
        private EmbeddingRepository embeddingRepository;

        public TransactionEventsConsumerService(FraudDetectionAnalyzerService fraudDetectionAnalyzerService,
                        FraudAnalysisResultRepository fraudAnalysisResultRepository,
                        FraudEventPublisher FraudEventPublisher,
                        FraudDetectionApplication fraudDetectionApplication,
                        EmbeddingRepositoryCustomImpl customEmbeddingRepository,
                        EmbeddingService embeddingService,
                        EmbeddingRepository embeddingRepository, TravelAnalyzer travelAnalyzer) {
                this.fraudDetectionAnalyzerService = fraudDetectionAnalyzerService;
                this.fraudAnalysisResultRepository = fraudAnalysisResultRepository;
                this.FraudEventPublisher = FraudEventPublisher;
                this.fraudDetectionApplication = fraudDetectionApplication;
                this.customEmbeddingRepository = customEmbeddingRepository;
                this.embeddingService = embeddingService;
                this.embeddingRepository = embeddingRepository;
                this.travelAnalyzer = travelAnalyzer;
        }

        @KafkaListener(topics = TOPIC, groupId = "fraud-detection-group", containerFactory = "kafkaTransactionListenerContainerFactory")
        public void listenToTopic(KafkaTransactionCreatedEvent createdEvent) throws Exception {
                System.out.print("Recieved event from transaction ID: " + createdEvent.getTransactionId()
                                + " Customer : " + createdEvent.getCustomerId());

                // sends it to be analyzed iwth our own logic
                FraudAnalysisResult analysis_result = fraudDetectionAnalyzerService.isFraud(createdEvent);

                fraudAnalysisResultRepository.save(analysis_result);

                System.out.println("The current Fraud Result being anayzed rn is of Id: " + analysis_result.getId());
                // embedds and saves
                TransactionEmbedding embedding_result = embeddingService.embedTransaction(createdEvent,
                                analysis_result);

                List<SimilarTransactionDTO> similarTransactions = customEmbeddingRepository
                                .findSimilar(embedding_result.getEmbedding(), 5);

                embeddingRepository.save(embedding_result);

                if (analysis_result.getRiskLevel() == LEVEL.CRITICAL || analysis_result.getRiskLevel() == LEVEL.HIGH) {
                        analysis_result.setStatus(STATUS.FRAUD);

                }

                long highRiskCount = similarTransactions.stream()
                                .filter(t -> t.transactionEmbedding().getAnalysisResult().getRiskLevel() == LEVEL.HIGH
                                                ||
                                                t.transactionEmbedding().getAnalysisResult()
                                                                .getRiskLevel() == LEVEL.CRITICAL)
                                .count();
                if (highRiskCount >= 2) {
                        if (analysis_result.getRiskLevel() == LEVEL.LOW) {
                                analysis_result.setRiskLevel(LEVEL.MEDIUM);
                        }
                        if (analysis_result.getRiskLevel() == LEVEL.MEDIUM) {
                                analysis_result.setRiskLevel(LEVEL.HIGH);
                        }
                }

                // printSimilarTransactions(similarTransactions); //testing purposes

                if (analysis_result.getRiskLevel() == LEVEL.HIGH || analysis_result.getRiskLevel() == LEVEL.CRITICAL) {
                        System.out.println("check");
                        analysis_result.setStatus(STATUS.FRAUD);
                } else {
                        analysis_result.setStatus(STATUS.NOT_FRAUD);
                }

                if (analysis_result.getStatus() == STATUS.FRAUD) {
                        System.out.println("and check");
                        FraudEventPublisher.PublishEvent(analysis_result);
                        System.out.println("last check");
                }

        }

        @Transactional
        public void printSimilarTransactions(List<SimilarTransactionDTO> similarTransactions) {

                System.out.println("The 5 closest Transactions to this ones are ... ");
                for (int i = 0; i < similarTransactions.size(); i++) {

                        FraudAnalysisResult thisResult = similarTransactions.get(i).transactionEmbedding()
                                        .getAnalysisResult();

                        System.out.println("Transaction Id: " + thisResult.getTransaction_id()
                                        + " Distance: " + similarTransactions.get(i).distance() +
                                        " Similarity " + similarTransactions.get(i).similarity() +
                                        "Fraud level : " + similarTransactions.get(i).transactionEmbedding()
                                                        .getAnalysisResult().getRiskLevel().toString());

                }
        }

}
