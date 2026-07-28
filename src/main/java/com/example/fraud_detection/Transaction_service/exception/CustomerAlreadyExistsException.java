package com.example.fraud_detection.Transaction_service.exception;

public class CustomerAlreadyExistsException extends Exception {

    public CustomerAlreadyExistsException(){
        super("Customer Already Exists");
    }
    
    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}
