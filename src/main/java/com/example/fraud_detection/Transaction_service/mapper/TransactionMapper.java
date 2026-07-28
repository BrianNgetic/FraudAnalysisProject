package com.example.fraud_detection.Transaction_service.mapper;




import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.entity.*;

import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Country;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.Currency;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.MerchantCategory;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.TransactionStatus;
import com.example.fraud_detection.Transaction_service.dtos.*;
import com.example.fraud_detection.Transaction_service.repository.CustomerRepository;

import com.example.fraud_detection.Transaction_service.exception.*;

@Component
public class TransactionMapper {
    

    private final CustomerRepository customerRepository;

    TransactionMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //validates the transactiondto from the user and returns an actual transaction
    public Transaction fromUserCreatedTransactionToTransaction(CreatedTransactionRequestDto requestDto) throws Exception
    {
    
        //first validate if the customer exists
        if(!customerRepository.existsById(requestDto.getCustomerId())){
                throw new CustomerNotFoundException();
        }
        Customer customer = customerRepository.getReferenceById(requestDto.getCustomerId());

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

        MerchantCategory verifiedMerchantCategory  = MerchantCategory.fromString(requestDto.getMerchantCategoryString());
        if(verifiedMerchantCategory  == null){
            throw new InvalidMerchantCategoryException();
        }


        Transaction newTransaction = new Transaction();
        newTransaction.setCustomer(customer);
        newTransaction.setAmount(requestDto.getTransaction_amount());
        newTransaction.setCurrency(verifiedCurrency.get());
        newTransaction.setCountry(verifiedCountry);
        newTransaction.setMerchantCategory(verifiedMerchantCategory);
        newTransaction.setDeviceId(requestDto.getDeviceId());
        newTransaction.setTimestamp(LocalDateTime.now());
        newTransaction.setTransactionStatus(TransactionStatus.PENDING);
        return newTransaction;

        




    }

    public Customer fromUserCreatedCustomerToCustomer(CreatedCustomerRequestDto customerDto){
        

        Customer newCustomer = new Customer();
        newCustomer.setName(customerDto.getName());
        newCustomer.setEmail(customerDto.getEmail());
        newCustomer.setCreatedAt(LocalDateTime.now());

        return newCustomer;
    }

    
}
