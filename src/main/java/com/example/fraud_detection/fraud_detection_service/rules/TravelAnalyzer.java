package com.example.fraud_detection.fraud_detection_service.rules;

import java.time.*;

import org.springframework.stereotype.Component;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;


import com.example.fraud_detection.fraud_detection_service.entity.TravelResult;
import com.example.fraud_detection.Transaction_service.entity.Transaction;

@Component
public class TravelAnalyzer {

    private static final double EARTH_RADIUS = 6371.0;
    private static final double MAX_REASONABLE_TRAVEL_SPEED = 1000; //KMH

    public TravelResult analyze(Transaction lastTransaction, KafkaTransactionCreatedEvent currentTransaction ){
        
            double distanceBetweenTransactionsInKm =   calculateDistance(lastTransaction, currentTransaction);
            double hoursElapsed = calculateHoursElapsed(lastTransaction, currentTransaction);
            double requiredSpeed = distanceBetweenTransactionsInKm / hoursElapsed;
            
            boolean possibleTravel;
            if(requiredSpeed < MAX_REASONABLE_TRAVEL_SPEED ){possibleTravel = true;}
            else{possibleTravel  = false;}
            TravelResult result = TravelResult.builder()
                                    .distanceInKm(distanceBetweenTransactionsInKm)
                                    .hoursElapsed(hoursElapsed)
                                    .requiredSpeed(requiredSpeed)
                                    .possibleTravel(possibleTravel)
                                    .build();


         

            return result;



    }


    
    public static Double calculateDistance(Transaction lastTransaction, KafkaTransactionCreatedEvent currentTransaction){
        
        
        double lat1Rad = Math.toRadians(lastTransaction.getLatitude());
        double lat2Rad = Math.toRadians(currentTransaction.getLatitude());

        double long1Rad  = Math.toRadians(lastTransaction.getLongitude());
        double long2Rad = Math.toRadians(lastTransaction.getLongitude());


        double deltaLat = lat2Rad - lat1Rad;
        double deltaLong = long2Rad - long1Rad;
        

        //haversite math formula

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + 
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2);

        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 -a));

    
        //returns the distance in km
        return EARTH_RADIUS * c;
    } 
    
    public double  calculateHoursElapsed(Transaction lastTransaction, KafkaTransactionCreatedEvent currentTransaction )
    {


        
         Instant lastTransactionInstant = lastTransaction
            .getTimestamp()
            .atZone(ZoneId.of("America/Chicago"))
            .toInstant();

        System.out.println("Last transaction happened at: " + lastTransaction.getTimestamp());

        Instant currentTransactionInstant =  currentTransaction
            .getTimestamp()
            .atZone(ZoneId.of("America/Chicago"))
            .toInstant();
        
        System.out.println("The current transaction is from : " + currentTransaction.getTimestamp());
        
        //returns the time between transaction in fractional hours, so 1.5 hrs, 2.1, etc
         return  Duration.between(lastTransactionInstant, currentTransactionInstant).toNanos() / 3600000000000.0;

    } 
}
