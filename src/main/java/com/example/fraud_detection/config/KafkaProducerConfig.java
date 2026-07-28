package com.example.fraud_detection.config;

import java.util.*;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.kafka.KafkaFraudCreatedEvent;

import io.micrometer.common.lang.NonNull;





//also not needed yet, doing auto config for a start
@Configuration
public class KafkaProducerConfig {
  
    //holds server info
        @Value("${spring.kafka.bootstrap-servers}")
        private  String bootstrapServers;

        @Bean
        public ProducerFactory<String, KafkaTransactionCreatedEvent> TransactionEventProducerFactory() {
            Map<String, Object> config = new HashMap<>();

            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

            return new DefaultKafkaProducerFactory<>(config);
        }



        @Bean
        public KafkaTemplate<String, KafkaTransactionCreatedEvent> kafkaTransactionTemplate(
          @NonNull ProducerFactory<String, KafkaTransactionCreatedEvent> producerFactory
        ){
            return new KafkaTemplate<>(producerFactory);
        }

        // will add one for fraudEvent later
        @Bean
         public ProducerFactory<String, KafkaFraudCreatedEvent> FraudEventProducerFactory(){
            Map<String, Object> config= new HashMap<>();

            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


            return new DefaultKafkaProducerFactory<>(config);
    
        }

        @Bean
        public KafkaTemplate<String, KafkaFraudCreatedEvent> kafkaFraudTemplate(
            @NonNull ProducerFactory<String, KafkaFraudCreatedEvent> producerFactory){
                    return new KafkaTemplate<>(FraudEventProducerFactory());
            }
        




}
