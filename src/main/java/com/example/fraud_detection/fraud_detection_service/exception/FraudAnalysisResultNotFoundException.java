package com.example.fraud_detection.fraud_detection_service.exception;

public class FraudAnalysisResultNotFoundException extends  Exception{
    public FraudAnalysisResultNotFoundException(){
        super("Fraud analusis Result Not Found");
    }
    public FraudAnalysisResultNotFoundException(String message){
        super(message);
    }

    
}
