package com.example.fraud_detection.alert_service.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;


import com.example.fraud_detection.fraud_detection_service.repository.FraudAnalysisResultRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
import com.example.fraud_detection.alert_service.dto.UpdateAlertStatusRequest;





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

        public  AlertRequestDto getSingleAlert(Long Id) throws Exception{
            
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

        public  Page<AlertRequestDto> getLatestAlerts(){

            Pageable latestFirst = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<Alert> dashBoardAlertPage = alertRepository.findLatestAlerts(latestFirst);


            Page<AlertRequestDto> dtoPage = dashBoardAlertPage.map(alert ->  alertMapper.AlertToAlertRequestMapper(alert));            


            
            return dtoPage;
        }
        

        public Page<AlertRequestDto> getAlertsInPages(int page){
            Pageable currentPage = PageRequest.of(page - 1, 10);

             return alertRepository.findAll(currentPage).map(alert ->alertMapper.AlertToAlertRequestMapper(alert));
        }
        

         public void patchAlertRequest(UpdateAlertStatusRequest updatedAlertStatus, Long Id) throws Exception{
            if(Id == null){
                throw new IllegalArgumentException("Alert ID must not be null");
            }
          
            Optional<Alert> alertOpt = alertRepository.findById(Id);
            if(alertOpt.isEmpty()){
                throw new AlertNotFoundException();
            }
            else{
                Alert thisAlert = alertOpt.get();
                thisAlert.setStatus(alertMapper.mapStringToStatus(updatedAlertStatus.getUpdatedStatus()));
        
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