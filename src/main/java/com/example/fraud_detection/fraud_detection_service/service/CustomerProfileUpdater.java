package com.example.fraud_detection.fraud_detection_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fraud_detection.fraud_detection_service.entity.CustomerCountryFrequency;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerMerchantFrequency;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerProfile;

import com.example.fraud_detection.fraud_detection_service.repository.CustomerCountryFrequenciesRepository;
import com.example.fraud_detection.fraud_detection_service.repository.CustomerMerchantFrequenciesRepository;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;

@Service
public class CustomerProfileUpdater {

    private final CustomerCountryFrequenciesRepository customerCountryFrequenciesRepository;
    private final CustomerMerchantFrequenciesRepository customerMerchantFrequenciesRepository;
    public CustomerProfileUpdater(
             CustomerCountryFrequenciesRepository customerCountryFrequenciesRepository,
                CustomerMerchantFrequenciesRepository customerMerchantFrequenciesRepository
    ){
        this.customerCountryFrequenciesRepository = customerCountryFrequenciesRepository;
        this.customerMerchantFrequenciesRepository = customerMerchantFrequenciesRepository;
    }
    

    public void updateProfile(KafkaTransactionCreatedEvent event,CustomerProfile customerProfile){


        customerProfile.setTotalTransactions(customerProfile.getTotalTransactions() + 1);


        boolean countryFound = false;
        boolean merchantFound =  false;

        List<CustomerCountryFrequency> thisCustomerCountryFrequeuncies =  customerProfile.getCountryFrequencies();
        List<CustomerMerchantFrequency> thisCustomerMerchantFrequencies = customerProfile.getMerchantFrequencies();
        
        if(thisCustomerCountryFrequeuncies == null){

            thisCustomerCountryFrequeuncies = new ArrayList<CustomerCountryFrequency>();
            CustomerCountryFrequency newCountryFrequency =  new CustomerCountryFrequency();
            newCountryFrequency.setCountry(event.getCountry());
            newCountryFrequency.setCustomerProfile(customerProfile);
            newCountryFrequency.setLastUpdated(LocalDateTime.now());
            newCountryFrequency.incrementTransactionCount(); 
            thisCustomerCountryFrequeuncies.add(newCountryFrequency);
            customerCountryFrequenciesRepository.save(newCountryFrequency);
            customerProfile.setCountryFrequencies(thisCustomerCountryFrequeuncies);
        }  
        
        
        if(thisCustomerMerchantFrequencies == null){

            thisCustomerMerchantFrequencies = new ArrayList<CustomerMerchantFrequency>();
            CustomerMerchantFrequency newMerchantFrequency = new CustomerMerchantFrequency();
            newMerchantFrequency.setMerchant(event.getMerchant());
            newMerchantFrequency.setCustomerProfile(customerProfile);
            newMerchantFrequency.setLastUpdated(LocalDateTime.now());
            newMerchantFrequency.incrementTransactionCount();
            thisCustomerMerchantFrequencies.add(newMerchantFrequency);
            customerMerchantFrequenciesRepository.save(newMerchantFrequency);
            customerProfile.setMerchantFrequencies(thisCustomerMerchantFrequencies);
        }  

        for(CustomerCountryFrequency countryFrequency : thisCustomerCountryFrequeuncies){

            
            if(countryFrequency.getCountry().equals(event.getCountry())){

                countryFrequency.incrementTransactionCount();

                countryFound = true;
                break;
            }

        
        }

        for(CustomerMerchantFrequency merchantFrequency : thisCustomerMerchantFrequencies){
            if(merchantFrequency.getMerchant().equals(event.getMerchantCategory())){

                merchantFrequency.incrementTransactionCount();

                merchantFound = true;
                break;

            }
        }

        if(!countryFound){
            CustomerCountryFrequency newCountryFrequency =  new CustomerCountryFrequency();
            newCountryFrequency.setCountry(event.getCountry());
            newCountryFrequency.setCustomerProfile(customerProfile);
            newCountryFrequency.setLastUpdated(LocalDateTime.now());
            newCountryFrequency.incrementTransactionCount(); //makes it 1 since its a long not Long
            thisCustomerCountryFrequeuncies.add(newCountryFrequency);
        }
        if(!merchantFound){
            CustomerMerchantFrequency  newCustomerMerchantFrequency = new CustomerMerchantFrequency();
            newCustomerMerchantFrequency.setMerchant(event.getMerchant());
            newCustomerMerchantFrequency.setCustomerProfile(customerProfile);
            newCustomerMerchantFrequency.setLastUpdated(LocalDateTime.now());
            newCustomerMerchantFrequency.incrementTransactionCount();
            thisCustomerMerchantFrequencies.add(newCustomerMerchantFrequency);

        }


        customerProfile.getCountryFrequencies().stream() 
                                                        .max(Comparator.comparingLong(CustomerCountryFrequency::getTransactionCount))
                                                        .ifPresent(topCountry -> customerProfile.setHomeCountry(topCountry.getCountry()));

        customerProfile.setLast_updated(LocalDateTime.now());
    }

    
}
