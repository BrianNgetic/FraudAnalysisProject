package com.example.fraud_detection.Transaction_service.exception;

public class InvalidMerchantCategoryException  extends Exception{

    public InvalidMerchantCategoryException(){
        super("Invalid Merchant Category");
    }

    public InvalidMerchantCategoryException(String message){
        super(message);
    }
    
}
