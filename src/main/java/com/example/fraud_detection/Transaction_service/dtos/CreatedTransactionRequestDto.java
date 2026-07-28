package com.example.fraud_detection.Transaction_service.dtos;


import io.micrometer.common.lang.NonNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CreatedTransactionRequestDto{
//this builds a transaction from customer input, includes their id 
    @NonNull
    private Long customerId;

    private double transaction_amount;

    private String currencyString;

    private String merchantCategoryString;

    private String country;

    private String deviceId;

    public CreatedTransactionRequestDto(
        Long customerId, double transaction_amount, String currencyString,  
        String merchantCategoryString, String country, String deviceId
    ){
        this.customerId = customerId;
        this.transaction_amount = transaction_amount;
        this.currencyString  = currencyString;
        this.merchantCategoryString = merchantCategoryString;
        this.country = country;
        this.deviceId = deviceId;
    }




}