package com.example.fraud_detection.Transaction_service.exception;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(){
        super("Customer Not Found");
    }    
    public CustomerNotFoundException(String message){
        super(message);
    }
}
