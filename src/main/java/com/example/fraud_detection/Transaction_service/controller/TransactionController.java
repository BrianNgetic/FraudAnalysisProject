package com.example.fraud_detection.Transaction_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.Transaction_service.service.TransactionService;
import com.example.fraud_detection.Transaction_service.dtos.*;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;



@RestController
@RequestMapping("/transactions")
public class TransactionController {

    public TransactionService transactionService;

    public TransactionController(TransactionService transactionService)
   {
    this.transactionService = transactionService;
   }
    
    
    @PostMapping("/create")
    public ResponseEntity<?> populateTransactions(@RequestBody List<CreatedTransactionRequestDto> transactionDtos) throws Exception {

      
        transactionService.CreateTransaction(transactionDtos);
      
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDtos);


        
    }

    //currently this is for testing, normally when you create a transaction it publishes automatically
    @PostMapping("/publish/{Id}")
    public ResponseEntity<?> publishTransaction(@PathVariable Long Id) throws Exception{

        transactionService.publicTransaction(Id);

        return ResponseEntity.status(HttpStatus.CREATED).body(Id);

    }
    //get transactions of a certain id
    @GetMapping ("/Id")
    public  ResponseEntity<Transaction> getTransactions(@PathVariable Long Id)  throws Exception
    {
       
        //transactionrepository.findbyid...
        return ResponseEntity.ok(transactionService.getTransaction(Id));
    }
    

    //get a customers transaction
    @GetMapping("/customer/{id}/transactions")
    public ResponseEntity<List<Transaction>> getCustomersTransactions(@PathVariable Long Id) throws Exception
     {
        //returns a list of transactions linked to the customer with the following id

        //in my implementation, i have to do transactionresitory.findbycustomer..
        
        return  ResponseEntity.ok(transactionService.getAllCustomersTransactions(Id));
    }
    
    
}
