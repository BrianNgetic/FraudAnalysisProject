package com.example.fraud_detection.fraud_detection_service.rules;

import java.time.*;
import java.util.List;

import com.example.fraud_detection.fraud_detection_service.entity.TransactionHistory;
public class VelocityRules {

    
   public int velocityAnalyzer(List<TransactionHistory> recentTransactions){
        
        int velocityAnalysisScoreResult  = 0;

        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.ofHours(1));
    
        Long numberofTransactionsInThePastHour = recentTransactions.stream().filter(e -> {
            Instant t = e.getTimestamp().atZone(ZoneId.of("America/Chicago"))
                        .toInstant();
            return !t.isBefore(oneHourAgo) && t.isAfter(now);
        }).count();

        if(numberofTransactionsInThePastHour > 6 &&  numberofTransactionsInThePastHour <= 10){velocityAnalysisScoreResult += 10;}
        else if(numberofTransactionsInThePastHour >10 && numberofTransactionsInThePastHour <= 20 ){velocityAnalysisScoreResult += 18;}
        else if(numberofTransactionsInThePastHour >20) { velocityAnalysisScoreResult+= 25;}

        return velocityAnalysisScoreResult;
    }


    
    
}
