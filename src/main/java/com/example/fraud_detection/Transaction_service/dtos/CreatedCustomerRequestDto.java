package com.example.fraud_detection.Transaction_service.dtos;



public class CreatedCustomerRequestDto {
    
    private String name;
    private String email;

    public String getName(){
        return this.name;
    }

   public String getEmail(){
        return this.email;
    }
}
