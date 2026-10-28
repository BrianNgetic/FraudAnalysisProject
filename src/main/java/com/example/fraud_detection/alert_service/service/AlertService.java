package com.example.fraud_detection.alert_service.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.example.fraud_detection.fraud_detection_service.repository.FraudAnalysisResultRepository;

import groovyjarjarantlr4.v4.codegen.SourceGenTriggers.alternative_return;

import com.example.fraud_detection.alert_service.repository.AlertRepository;
import com.example.fraud_detection.fraud_detection_service.exception.FraudAnalysisResultNotFoundException;
import com.example.fraud_detection.alert_service.exception.AlertNotFoundException;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.LEVEL;
import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;
import com.example.fraud_detection.alert_service.entity.Alert;
import com.example.fraud_detection.alert_service.entity.Alert.AlertSeverity;
import com.example.fraud_detection.alert_service.entity.Alert.AlertStatus;
import com.example.fraud_detection.alert_service.dto.AlertRequestDto;
import com.example.fraud_detection.alert_service.mapper.AlertMapper;




@Service 
public class AlertService {


        private final FraudAnalysisResultRepository fraudAnalysisResultRepository;
        private final AlertRepository alertRepository;
        private final AlertMapper alertMapper;

        public AlertService(FraudAnalysisResultRepository fraudAnalysisResultRepository,
                            AlertRepository alertRepository,
                            AlertMapper alertMapper){
            this.fraudAnalysisResultRepository = fraudAnalysisResultRepository;
            this.alertRepository = alertRepository;
            this.alertMapper  = alertMapper;
        }


        public Alert createAlert(FraudEvent fraudEvent) throws Exception{

            Long analysisResultId = fraudEvent.fraudAnalysisResultId();

            if(analysisResultId == null){
                throw new  FraudAnalysisResultNotFoundException();
            }
        
            FraudAnalysisResult analysisResult = fraudAnalysisResultRepository.getReferenceById(analysisResultId);

            Alert newAlert = Alert.builder()
                                .transactionId(fraudEvent.transactionId())
                                .fraudAnalysisResultId(analysisResultId)
                                .fraudEventId(fraudEvent.eventId())
                                .status(AlertStatus.NEW)
                                .severity(mapToSeverity(analysisResult.getRiskLevel()))
                                .title((analysisResult.getRiskLevel().toString() + "-risk detected"))
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .resolvedAt(null)
                                .build();

            return newAlert;
           
        }

        public  AlertRequestDto getAlert(Long Id) throws Exception{
            
            if(Id == null){
                throw new IllegalArgumentException("Alert ID must not be null");
            }
          
            Optional<Alert> alertOpt = alertRepository.findById(Id);
            if(alertOpt.isEmpty()){
                throw new AlertNotFoundException();
            }
            else{
                 return alertMapper.AlertToAlertRequestMapper(alertOpt.get());
            }
            
                
           
        }
        
        


        public static AlertSeverity mapToSeverity(LEVEL level) {
                if (level == null) return null;
                return switch (level) {
                    case LOW -> AlertSeverity.LOW;
                    case MEDIUM -> AlertSeverity.MEDIUM;
                    case HIGH -> AlertSeverity.HIGH;
                    case CRITICAL -> AlertSeverity.CRITICAL;
                };
        }
}