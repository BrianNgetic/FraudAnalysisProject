package com.example.fraud_detection.fraud_detection_service.mapper;


import com.example.fraud_detection.fraud_detection_service.dto.FraudAnalysisResultRequestDto;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;

import jakarta.transaction.Transactional;

import java.util.Optional;

import com.example.fraud_detection.fraud_detection_service.dto.TransactionRequestDto;
import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.Transaction_service.exception.TransactionNotFoundException;
import com.example.fraud_detection.Transaction_service.repository.TransactionRepository;
public class FraudAnalysisResultMapper {

    private final TransactionRepository transactionRepository;

    public FraudAnalysisResultMapper(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    //this combines both analysis and transactional data, for the transaction page
    public FraudAnalysisResultRequestDto FraudAnalysisResultToDtoMapper(FraudAnalysisResult analysisResult) throws Exception{
        
        Long thisAnalysisResultsTransactionId = analysisResult.getTransaction_id();
        
        if(thisAnalysisResultsTransactionId == null){
             throw new  TransactionNotFoundException("Transaction Id is Null");      
        }

         Optional<Transaction> optTransaction = transactionRepository.findById(thisAnalysisResultsTransactionId );
            
         if(optTransaction.isEmpty()){
            throw new TransactionNotFoundException();
         }
    

        Transaction thisAnalysisResultsTransaction = optTransaction.get();

        TransactionRequestDto transactionRequestDto  = new TransactionRequestDto(
                thisAnalysisResultsTransactionId,
                analysisResult.getCustomer_id(),
                thisAnalysisResultsTransaction.getAmount(),
                thisAnalysisResultsTransaction.getCurrency().toString(),
                thisAnalysisResultsTransaction.getMerchant(),
                thisAnalysisResultsTransaction.getTimestamp().toString()
        );


            return new FraudAnalysisResultRequestDto(
                    analysisResult.getId(),
                    analysisResult.getCustomer_id(),
                    analysisResult.getRiskLevel().toString(),
                    thisAnalysisResultsTransaction.getDeviceId(),
                    transactionRequestDto,
                    thisAnalysisResultsTransaction.getLongitude(),
                    thisAnalysisResultsTransaction.getLatitude()
        );




    }

}
