package com.example.fraud_detection.fraud_detection_service.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class TravelResult {


    private double distanceInKm;
    private double hoursElapsed;
    private double requiredSpeed;
    private boolean possibleTravel;
    
}
