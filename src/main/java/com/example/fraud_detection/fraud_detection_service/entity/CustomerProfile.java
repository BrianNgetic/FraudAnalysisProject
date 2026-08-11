package com.example.fraud_detection.fraud_detection_service.entity;

import java.time.LocalDateTime;
import java.util.List;


import com.example.fraud_detection.Transaction_service.repository.CustomerRepository;
import com.example.fraud_detection.Transaction_service.entity.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class CustomerProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private  Customer customer;

    private long totalTransactions; 

    private String HomeCountry;

    // private List<String> typicalMerchantCategories;

    // private  double averageSpendingAmount;

    
    private LocalDateTime last_updated;
    
    
            //maintains their most/frequent used phones, merchants
            //amd what countries they usually are at.
    @OneToMany(mappedBy = "customerProfile")
    private List<CustomerCountryFrequency> countryFrequencies;

    @OneToMany(mappedBy = "customerProfile")
    private List<CustomerMerchantFrequency> merchantFrequencies;
  
    @OneToMany(mappedBy = "customerProfile") 
    private List<CustomerDeviceFrequency> deviceFrequencies;

    

    public boolean isNewMarchant(String merchant){
        for(CustomerMerchantFrequency c: merchantFrequencies){
            if(c.getMerchant().equals(merchant)) return false;
        }
        return true;
    }

    public boolean isNewCountry(String country){

       for(CustomerCountryFrequency c: countryFrequencies){
            if(c.getCountry().equals(country)) return false;
        }

        return true;

    }
    
    public boolean isNewDevice(String device){
        for(CustomerDeviceFrequency d : deviceFrequencies){
            if(d.getDevice().equals(device)){
                return false;
            }
        }

        return true;
    }



   


    
    
}

