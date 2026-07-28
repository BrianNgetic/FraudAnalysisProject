package com.example.fraud_detection.Transaction_service.exception;

public class InvalidCurrencyException  extends Exception{

    public InvalidCurrencyException(){
        super("Currency not valid");
    }
    public InvalidCurrencyException(String message){
        super(message);
    }
    
}
