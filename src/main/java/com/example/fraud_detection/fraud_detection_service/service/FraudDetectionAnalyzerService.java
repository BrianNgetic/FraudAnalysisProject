package com.example.fraud_detection.fraud_detection_service.service;



import com.example.fraud_detection.fraud_detection_service.kafka.KafkaFraudCreatedEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.List;


import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.STATUS;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerProfile;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionHistory;
import com.example.fraud_detection.fraud_detection_service.rules.*;
import com.example.fraud_detection.fraud_detection_service.entity.TravelResult;

import com.example.fraud_detection.fraud_detection_service.repository.TransactionHistoryRepository;

import com.example.fraud_detection.fraud_detection_service.repository.CustomerProfileRepository;

@Service
public class FraudDetectionAnalyzerService {
    

    //run some logic on the transaction that you findbyif
    //set the status
    //saveit

    private final ConsumerFactory<String, KafkaFraudCreatedEvent> FraudEventConsumerFactory;
    private final CustomerProfileRepository customerProfileRepository;
    private final CustomerProfileUpdater customerProfileUpdater;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final VelocityRules velocityRules;
    private final TravelAnalyzer travelAnalyzer;





    public FraudDetectionAnalyzerService(CustomerProfileRepository customerProfileRepository,
                                 ConsumerFactory<String, KafkaFraudCreatedEvent> FraudEventConsumerFactory,
                                 CustomerProfileUpdater customerProfileUpdater,
                                 VelocityRules velocityRules,
                                 TravelAnalyzer travelAnalyzer,
                                 TransactionHistoryRepository transactionHistoryRepository){
        this.customerProfileRepository = customerProfileRepository;
        this.FraudEventConsumerFactory = FraudEventConsumerFactory; 
        this.velocityRules = velocityRules;
        this.travelAnalyzer = travelAnalyzer;
        this.customerProfileUpdater = customerProfileUpdater;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }



    public FraudAnalysisResult isFraud(KafkaTransactionCreatedEvent event){



       FraudAnalysisResult result = FraudAnalysisResult.builder()
                                        .transaction_id(event.getTransactionId())
                                        .customer_id(event.getCustomerId())
                                        .analyzed_at(LocalDateTime.now())
                                        .build();
       


        //update the customerprofile, first we need to retrieve it
        Optional<CustomerProfile> OptionalCustomersProfile = customerProfileRepository.findByCustomerId(event.getCustomerId());

        CustomerProfile thisCustomerProfile;

        if(OptionalCustomersProfile.isPresent()){
           thisCustomerProfile  = OptionalCustomersProfile.get();
        

        
        //first, lets load the customers previous transactions(last 100)
        List<TransactionHistory> recentTransactions = transactionHistoryRepository.findTop100ByCustomerIdOrderByTimestampDesc(event.getCustomerId());
        
        /*
        use this last 100 transactions to compare this transactions to the users recent transactions
        - we use this to determing
            0. what is the largest transaction
            1. what is the average time between the last 100, 50, 10 transactions
            2. what is the most frequent  merchant category, and place these transactions have been in
            3. what is the average time between those transactions, the smaller the higher the risk of fraud
            4. what is the velocity, meaning say 5 perchases in an hour is normal, 100 isnt
            
            
        */

        Integer RiskScore = 0;

        /*
            Next thing i want to define is some velocity rules
                0–5   ->   Normal , 0 points 
                6–15  -> Slightly unusual, 10 points
                10-20  -> Higher risk, 20 points
                20 +    -> most risk 35 points
        
        */
            
        Instant now = Instant.now();
        
        RiskScore += velocityRules.velocityAnalyzer(recentTransactions);


        TransactionHistory LastTransaction = recentTransactions.get(0);

       TravelResult travelAnalysisResult =  travelAnalyzer.analyze(LastTransaction, event);
        //one of the strongest indicators
       if(!travelAnalysisResult.isPossibleTravel()){
            RiskScore += 30;
            result.addReason("Impossible Travel detected");

        }


        


        //more than 2 or 3 countries is suspect for sure
        Long numberOfDifferentCountriesOverThePast100Transactions = recentTransactions.stream()
                                                                        .map(T -> T.getCountry() )
                                                                        .distinct()
                                                                        .count();

        //gues this shouldnt add as much, but still a factor
        Long numberOfDifferentMerchantsOverThePast100Transactions = recentTransactions.stream()
                                                                        .map(T -> T.getCategory())
                                                                        .distinct()
                                                                        .count();

        Instant lastTransactionInstant = recentTransactions.get(0)
            .getTimestamp()
            .atZone(ZoneId.of("America/Chicago"))
            .toInstant();



        //average amount spend over the past 100 transactions
        int totalAmount = 0;
        double maxAmount = 0;
        for(TransactionHistory H: recentTransactions){
            totalAmount += H.getAmount();
            if(H.getAmount() > maxAmount) maxAmount = H.getAmount();
        }
    
        double averageSpendOverPast100 = totalAmount / 100;

        if(event.getAmount() > (2 * averageSpendOverPast100) && event.getAmount() < (3 * averageSpendOverPast100)){ RiskScore += 10;}
        else if(event.getAmount() > (3 * averageSpendOverPast100) && event.getAmount() < (5 * averageSpendOverPast100)){ RiskScore += 15;}
        else if(event.getAmount() > (5 * averageSpendOverPast100)){ RiskScore += 20;}


        if(thisCustomerProfile.isNewDevice(event.getDevice())){RiskScore += 10;}
        if(thisCustomerProfile.isNewCountry(event.getCountry())){RiskScore += 15;}
        if(thisCustomerProfile.isNewMarchant(event.getMerchant())){RiskScore += 10;}


      
        //run logic first, then update the profile


        /*average time between each trasnaction , this will be added in v2
            //if it is less than a minute ago ..meh
            Duration timeSinceLastTransaction = Duration.between(lastTransactionInstant, now);
            // Long sumMinutes  = 0L;  
        
            // for(int i = 0; i < recentTransactions.size() -1; i++){
            //         sumMinutes += Duration.between(recentTransactions.get(i).getTimestamp() , recentTransactions.get(i+1).getTimestamp()).toMinutes();
            // }


            Long averageTimeBetweenRecentTransactions = sumMinutes /  recentTransactions.size() -1;

        */


        //looking at this funny for now, if its a new customer
        //there is nothing to compare to
        }else{
            thisCustomerProfile = new CustomerProfile();
            thisCustomerProfile.setCustomerId(event.getCustomerId());
        }
        //last step
        customerProfileUpdater.updateProfile(event , thisCustomerProfile);

     

        
       
       //first version of this, ill make it  just dumb, i just need it to work at first
       if(event.getAmount() > 1000){
            result.setRiskScore(50.00);
            result.setStatus(STATUS.FRAUD);
            result.addReason("Amount too high");
        
       }
       
            result.setRiskScore(0.00);
            result.setStatus(STATUS.NOT_FRAUD);
            result.addReason("no reason yet");
           
        
        return result;
    }
}
