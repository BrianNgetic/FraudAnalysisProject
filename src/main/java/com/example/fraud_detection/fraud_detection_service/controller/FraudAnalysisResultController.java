package com.example.fraud_detection.fraud_detection_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fraud_detection.fraud_detection_service.service.FraudanalysisResultService;
@RestController
@RequestMapping("/fraud-analysis")
public class FraudAnalysisResultController {

    private final FraudanalysisResultService fraudanalysisResultService;

    public FraudAnalysisResultController(FraudanalysisResultService fraudanalysisResultService){
        this.fraudanalysisResultService =  fraudanalysisResultService;
    }

    
}
