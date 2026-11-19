package com.example.fraud_detection.Transaction_service.mapper;




import java.time.LocalDateTime;
import java.util.Optional;


import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.entity.*;

import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Country;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Currency;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.MerchantCategory;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.TransactionStatus;
import com.example.fraud_detection.Transaction_service.dtos.*;
import com.example.fraud_detection.Transaction_service.repository.CustomerRepository;

import jakarta.transaction.Transactional;

import com.example.fraud_detection.Transaction_service.exception.*;

@Component
public class TransactionMapper {
    

    private final CustomerRepository customerRepository;

    TransactionMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //validates the transactiondto from the user and returns an actual transaction
    @Transactional
    public Transaction fromUserCreatedTransactionToTransaction(CreatedTransactionRequestDto requestDto) throws Exception
    {
    
        //first validate if the customer exists
        Customer customer;
      
        Optional<Customer> Optcustomer = customerRepository.findById(requestDto.getCustomerId());

        if(!Optcustomer.isPresent()){
            throw new CustomerNotFoundException();
        }
        else{
            customer = Optcustomer.get();
        }
        //validate the currency
        Optional<Currency> verifiedCurrency = Currency.fromString(requestDto.getCurrencyString());
        if(verifiedCurrency.isEmpty()){
            throw new InvalidCurrencyException();
        }
         
        //validate the country;
        Country verifiedCountry = Country.fromString(requestDto.getCountry());
        if(verifiedCountry == null){
            throw new InvalidCountryException();
        }

        //validate the category

        MerchantCategory verifiedMerchantCategory  = MerchantCategory.fromString(requestDto.getMerchantCategory());
        System.out.println("The current category is " + requestDto.getMerchantCategory());
        if(verifiedMerchantCategory  == null){
  
            throw new InvalidMerchantCategoryException();
        }


        Transaction newTransaction = new Transaction();
            newTransaction.setCustomer(customer);
            newTransaction.setAmount(requestDto.getTransaction_amount());
            newTransaction.setCurrency(verifiedCurrency.get());
            newTransaction.setCountry(verifiedCountry);
            newTransaction.setMerchant(requestDto.getMerchant());
            newTransaction.setMerchantCategory(verifiedMerchantCategory);
            newTransaction.setLatitude(requestDto.getLatitude());
            newTransaction.setLongitude(requestDto.getLongitude());
            newTransaction.setDeviceId(requestDto.getDeviceId());
            newTransaction.setTransactionStatus(TransactionStatus.APPROVED);
            newTransaction.setTimestamp(requestDto.getTimestamp());
            newTransaction.setTransactionStatus(TransactionStatus.PENDING);
        return newTransaction;

        




    }

    public Customer fromUserCreatedCustomerToCustomer(CreatedCustomerRequestDto customerDto){
        

      Customer newCustomer  = Customer.builder()
                                                .name(customerDto.getName())
                                                .email(customerDto.getEmail())
                                                .createdAt(LocalDateTime.now())
                                                .build();                    
        return newCustomer;
    }

    
}
