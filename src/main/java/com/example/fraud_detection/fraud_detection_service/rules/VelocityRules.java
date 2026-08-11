package com.example.fraud_detection.fraud_detection_service.rules;

import java.time.*;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.entity.Transaction;
@Component
public class VelocityRules {

    
   public List<Long> velocityAnalyzer(List<Transaction> recentTransactions){
        
        Long velocityAnalysisScoreResult  = 0L;

        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.ofHours(1));
    
        Long  numberofTransactionsInThePastHour = recentTransactions.stream().filter(e -> {
            Instant t = e.getTimestamp().atZone(ZoneId.of("America/Chicago"))
                        .toInstant();
            return !t.isBefore(oneHourAgo) && t.isAfter(now);
        }).count();

        if(numberofTransactionsInThePastHour > 6 &&  numberofTransactionsInThePastHour <= 10){velocityAnalysisScoreResult += 10;}
        else if(numberofTransactionsInThePastHour >10 && numberofTransactionsInThePastHour <= 20 ){velocityAnalysisScoreResult += 18;}
        else if(numberofTransactionsInThePastHour >20) { velocityAnalysisScoreResult+= 25;}

        return List.of(numberofTransactionsInThePastHour,   velocityAnalysisScoreResult);
    }


    
    
}
