package com.example.fraud_detection.Transaction_service.exception;

public class TransactionNotFoundException extends Exception{
    
    public TransactionNotFoundException(){
        super("Transaction Not Found");

    }
    public TransactionNotFoundException(String message){
        super(message);
    }
}
