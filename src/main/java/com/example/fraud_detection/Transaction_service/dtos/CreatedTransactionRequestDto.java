package com.example.fraud_detection.Transaction_service.dtos;


import java.time.LocalDateTime;

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

    private String merchant;

    private String merchantCategoryString;

    private String country;

    @NonNull
    private Double longitude;

    @NonNull
    private Double latitude;

    private String deviceId;

    private LocalDateTime timestamp;

    public CreatedTransactionRequestDto(
        Long customerId, double transaction_amount, String currencyString, String merchant, 
        String merchantCategoryString, String country, Double longitude,
        Double latitude, String deviceId, LocalDateTime timestamp
    ){
        this.customerId = customerId;
        this.transaction_amount = transaction_amount;
        this.currencyString  = currencyString;
        this.merchant = merchant;
        this.merchantCategoryString = merchantCategoryString;
        this.country = country;
        this.longitude = longitude;
        this.latitude = latitude;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }


    public Double getLatitude(){
        return this.latitude;
    }


}