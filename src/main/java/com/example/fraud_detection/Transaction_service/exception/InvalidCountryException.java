package com.example.fraud_detection.Transaction_service.exception;

public class InvalidCountryException  extends Exception{
    
    public InvalidCountryException(){
        super("Invalid Country");
    }
    public InvalidCountryException(String message){
        super(message);
    }
}
