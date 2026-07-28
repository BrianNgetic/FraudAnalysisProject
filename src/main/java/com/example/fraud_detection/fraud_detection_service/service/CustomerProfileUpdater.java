package com.example.fraud_detection.fraud_detection_service.service;

import java.util.List;

import com.example.fraud_detection.fraud_detection_service.entity.CustomerCountryFrequency;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerMerchantFrequency;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerProfile;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;

public class CustomerProfileUpdater {
    

    public void updateProfile(KafkaTransactionCreatedEvent event,CustomerProfile customerProfile){


        customerProfile.setTotalTransactions(customerProfile.getTotalTransactions() + 1);

        boolean countryFound = false;
        boolean merchantFound =  false;

        List<CustomerCountryFrequency> thisCustomersCountryFrequeuncy =  customerProfile.getCountryFrequencies();
        List<CustomerMerchantFrequency> thisCustomerMerchantFrequencies = customerProfile.getMerchantFrequencies();

        for(CustomerCountryFrequency countryFrequency : thisCustomersCountryFrequeuncy){

            if(countryFrequency.getCountry().equals(event.getCountry())){

                countryFrequency.incrementTransactionCount();

                countryFound = true;
                break;
            }
        
        }

        for(CustomerMerchantFrequency merchantFrequency : thisCustomerMerchantFrequencies){
            if(merchantFrequency.getMerchantCategory().equals(event.getMerchantCategory())){

                merchantFrequency.incrementTransactionCount();

                merchantFound = true;
                break;

            }
        }

        if(!countryFound){
            CustomerCountryFrequency newCountryFrequency =  new CustomerCountryFrequency();
            newCountryFrequency.setCustomerProfile(customerProfile);
            newCountryFrequency.incrementTransactionCount(); //makes it 1 since its a long not Long
        }
        if(!merchantFound){
            CustomerMerchantFrequency  newCustomerMerchantFrequency = new CustomerMerchantFrequency();
            newCustomerMerchantFrequency.setCustomerProfile(customerProfile);
            newCustomerMerchantFrequency.incrementTransactionCount();

        }

    }
}
