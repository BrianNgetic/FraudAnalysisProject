package com.example.fraud_detection.alert_service.mapper;

import com.example.fraud_detection.alert_service.entity.Alert;
import com.example.fraud_detection.alert_service.entity.Alert.AlertSeverity;
import com.example.fraud_detection.alert_service.entity.Alert.AlertStatus;

import org.springframework.stereotype.Component;

import com.example.fraud_detection.alert_service.dto.AlertRequestDto;

@Component 
public class AlertMapper {
    

    public AlertRequestDto AlertToAlertRequestMapper(Alert alert){

        return AlertRequestDto.builder()
                        .transactionId(alert.getTransactionId())
                        .fraudAnalysisResultId(alert.getFraudAnalysisResultId())
                        .fraudEventId(alert.getFraudEventId())
                        .status(mapToStatus(alert.getStatus()))
                        .severity(mapToSeverity(alert.getSeverity()))
                        .title(alert.getTitle())
                        .createdAt(alert.getCreatedAt())
                        .updatedAt(alert.getUpdatedAt())
                        .resolvedAt(alert.getResolvedAt())
                        .build();

    }

   



    public static String mapToSeverity(AlertSeverity severity){
        if(severity == null) return null;
        return switch(severity){
            case LOW -> "LOW";
            case MEDIUM -> "MEDIUM";
            case HIGH -> "HIGH";
            case  CRITICAL -> "CRITICAL";

        };

    }
     public static String mapToStatus(AlertStatus status){
        if(status == null ) return null;
        return switch(status){
          case   NEW -> "NEW";
          case   ACKNOWLEDGED -> "ACKNOWLEDGED";
          case  RESOLVED -> "RESOLVED";
        };
    }

    public  AlertStatus mapStringToStatus(String newStatus){
        if(newStatus == null || newStatus.isEmpty()){
            return null;
        }

        return switch(newStatus){
            case "NEW" -> AlertStatus.NEW;
            case "ACKNOWLEDGED" -> AlertStatus.ACKNOWLEDGED;
            case  "RESOLVED" -> AlertStatus.RESOLVED;
            default -> throw new IllegalArgumentException("Unknown alert status: " + newStatus);
        };

    }
     




}
