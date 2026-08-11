package com.example.fraud_detection.fraud_detection_service.service;



import com.example.fraud_detection.fraud_detection_service.kafka.KafkaFraudCreatedEvent;

import java.lang.System.Logger.Level;
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

import com.example.fraud_detection.Transaction_service.repository.TransactionRepository;
import com.example.fraud_detection.Transaction_service.entity.Transaction;
import com.example.fraud_detection.fraud_detection_service.entity.CustomerProfile;
import com.example.fraud_detection.fraud_detection_service.entity.TransactionHistory;
import com.example.fraud_detection.fraud_detection_service.rules.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import com.example.fraud_detection.fraud_detection_service.entity.TravelResult;
import com.example.fraud_detection.fraud_detection_service.entity.FraudAnalysisResult.LEVEL;
import com.example.fraud_detection.fraud_detection_service.repository.TransactionHistoryRepository;
import com.example.fraud_detection.Transaction_service.entity.Customer;

import com.example.fraud_detection.fraud_detection_service.repository.CustomerProfileRepository;

@Service
public class FraudDetectionAnalyzerService {
    

    //run some logic on the transaction that you findbyif
    //set the status
    //saveit

    private final ConsumerFactory<String, KafkaFraudCreatedEvent> FraudEventConsumerFactory;
    private final CustomerProfileRepository customerProfileRepository;
    private final CustomerProfileUpdater customerProfileUpdater;
    private final TransactionRepository transactionRepository;
    private final VelocityRules velocityRules;
    private final TravelAnalyzer travelAnalyzer;





    public FraudDetectionAnalyzerService(CustomerProfileRepository customerProfileRepository,
                                 ConsumerFactory<String, KafkaFraudCreatedEvent> FraudEventConsumerFactory,
                                 CustomerProfileUpdater customerProfileUpdater,
                                 VelocityRules velocityRules,
                                 TravelAnalyzer travelAnalyzer,
                                 TransactionRepository transactionRepository){
        this.customerProfileRepository = customerProfileRepository;
        this.FraudEventConsumerFactory = FraudEventConsumerFactory; 
        this.customerProfileUpdater = customerProfileUpdater;
        this.velocityRules = velocityRules;
        this.travelAnalyzer = travelAnalyzer;
        this.transactionRepository = transactionRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public FraudAnalysisResult isFraud(KafkaTransactionCreatedEvent event){



       FraudAnalysisResult result = FraudAnalysisResult.builder()
                                        .transaction_id(event.getTransactionId())
                                        .customer_id(event.getCustomerId())
                                        .analyzed_at(LocalDateTime.now())
                                        .build();



        //update the customerprofile, first we need to retrieve it
        Optional<CustomerProfile> OptionalCustomersProfile = customerProfileRepository.findByCustomerId(event.getCustomerId());


        if(OptionalCustomersProfile.isEmpty())
        {

            CustomerProfile thisCustomerProfile = new CustomerProfile();
            
            Customer customerProxy  = entityManager.getReference(Customer.class, event.getCustomerId());
            thisCustomerProfile.setCustomer(customerProxy);
            customerProfileRepository.save(thisCustomerProfile);
            customerProfileUpdater.updateProfile(event , thisCustomerProfile);
        }
        else
        {
        //placeholder

            CustomerProfile thisCustomerProfile  = OptionalCustomersProfile.get();

            //first, lets load the customers previous transactions(last 100)
            List<Transaction> recentTransactions = transactionRepository.findTop100ByCustomerIdOrderByTimestampDesc(event.getCustomerId());

            if(recentTransactions.isEmpty()){
                //profile exists but thiere is no history
                customerProfileUpdater.updateProfile(event , thisCustomerProfile);

            }
            else
            {
            


                Long RiskScore = 0L;

                
                result.setVelocity(velocityRules.velocityAnalyzer(recentTransactions).get(0));
                RiskScore += velocityRules.velocityAnalyzer(recentTransactions).get(1);
                

                Transaction LastTransaction = recentTransactions.get(0);
            
                TravelResult travelAnalysisResult =  travelAnalyzer.analyze(LastTransaction, event);
                //one of the strongest indicators
                if(!travelAnalysisResult.isPossibleTravel()){
                    RiskScore += 30;
                    result.setImpossibleTravel(true);
                    result.addReason("Impossible Travel detected");
                    

                }

                //average amount spend over the past 100 transactions
                int totalAmount = 0;
                double maxAmount = 0;
                for(Transaction H: recentTransactions){
                    totalAmount += H.getAmount();
                    if(H.getAmount() > maxAmount) maxAmount = H.getAmount();
                }
            
                double averageSpendOverPast100 = totalAmount / 100;

                if(event.getAmount() > (2 * averageSpendOverPast100) && event.getAmount() < (3 * averageSpendOverPast100)){ RiskScore += 10;}
                else if(event.getAmount() > (3 * averageSpendOverPast100) && event.getAmount() < (5 * averageSpendOverPast100)){ RiskScore += 15;}
                else if(event.getAmount() > (5 * averageSpendOverPast100)){ RiskScore += 20;}


                if(thisCustomerProfile.isNewDevice(event.getDevice())){RiskScore += 10; result.setIsNewDevice(true);}
                if(thisCustomerProfile.isNewCountry(event.getCountry())){RiskScore += 15;}
                if(thisCustomerProfile.isNewMarchant(event.getMerchant())){RiskScore += 10; result.setIsNewDevice(true);}

        
                if(RiskScore <= 29){result.setRiskLevel(LEVEL.LOW);}
                else if(RiskScore > 29 && RiskScore <  60){result.setRiskLevel(LEVEL.MEDIUM);}
                else if(RiskScore >= 60 && RiskScore < 80){result.setRiskLevel(LEVEL.HIGH);}
                else{result.setRiskLevel(LEVEL.CRITICAL);}


                result.setRiskScore(RiskScore);

                customerProfileUpdater.updateProfile(event , thisCustomerProfile);
            }

        }

        return result;
    }

}
