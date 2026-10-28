package com.example.fraud_detection.alert_service.exception;

public class AlertNotFoundException extends Exception {

    public AlertNotFoundException(){
        super("Alert Not Found");
    }
    

    public AlertNotFoundException(String message){
        super(message);
    }
}  