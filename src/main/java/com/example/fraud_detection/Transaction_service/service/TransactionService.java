package com.example.fraud_detection.Transaction_service.service;


import org.springframework.stereotype.Service;

import java.util.List;

import com.example.fraud_detection.Transaction_service.repository.*;
import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.Transaction_service.entity.TransactionEnums.TransactionStatus;
import com.example.fraud_detection.Transaction_service.exception.CustomerNotFoundException;
import com.example.fraud_detection.Transaction_service.exception.TransactionNotFoundException;
import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionEventsPublisher;
import com.example.fraud_detection.Transaction_service.dtos.*;
import com.example.fraud_detection.Transaction_service.mapper.*;


@Service
public class TransactionService {


    private TransactionRepository transactionRepository;
    private CustomerRepository customerRepository;
    private TransactionMapper transactionMapper;
    private KafkaTransactionEventsPublisher kafkaTransactionEventsPublisher;

    //add a kaflatamplate and a sendmessagetotpic method

  

    public TransactionService(TransactionRepository transactionRepository,
                                TransactionMapper transactionMapper,
                                KafkaTransactionEventsPublisher kafkaTransactionEventsPublisher){
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.kafkaTransactionEventsPublisher = kafkaTransactionEventsPublisher;
    }


    
    public void CreateTransaction(List<CreatedTransactionRequestDto> createdTransactionRequestDtos) throws Exception
    {
        //created and saved a transaction
        //the mapper here creates a validated transaction from the dto from the user
        for(CreatedTransactionRequestDto dto: createdTransactionRequestDtos){
             Transaction savedTransaction =  transactionRepository.saveAndFlush(transactionMapper.fromUserCreatedTransactionToTransaction(dto));
            
             //publish the event
            try{kafkaTransactionEventsPublisher.publishTransaction(savedTransaction);
            //if successfull
            savedTransaction.setTransactionStatus(TransactionStatus.PENDING);
            }catch (Exception e){
                    
                    e.printStackTrace();
            }
        }
        //return some typa responce from this
        
    }

    public void publicTransaction(long Id) throws Exception{
         if(!transactionRepository.existsById(Id)){
            throw new TransactionNotFoundException();
        }
        System.out.println("about to publish transaction of id: " + Id);
        Transaction thisTransaction = transactionRepository.getReferenceById(Id);
        kafkaTransactionEventsPublisher.publishTransaction(thisTransaction);
    }



    public Transaction getTransaction(Long id) throws Exception{
        if(!transactionRepository.existsById(id)){
            throw new TransactionNotFoundException();
        }

        return transactionRepository.getReferenceById(id);
    }

    public List<Transaction> getAllCustomersTransactions(Long id) throws Exception{

        if(!customerRepository.existsById(id)){
            throw new CustomerNotFoundException();
        }

        return transactionRepository.findByCustomerId(id);
    }




   
}
