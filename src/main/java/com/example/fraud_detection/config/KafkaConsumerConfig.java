package com.example.fraud_detection.config;

import java.util.*;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.fraud_detection.Transaction_service.kafka.KafkaTransactionCreatedEvent;
import com.example.fraud_detection.fraud_detection_service.entity.FraudEvent;

//not needed currently, doing auto configuration since this is basic  for now
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    // transactionEventConsumer
    @Bean
    public ConsumerFactory<String, KafkaTransactionCreatedEvent> TransactionEventConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "fraud-detection-group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(),
                new JsonDeserializer<>(KafkaTransactionCreatedEvent.class));
    }
    // FraudEventConsumer

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaTransactionCreatedEvent> kafkaTransactionListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, KafkaTransactionCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(TransactionEventConsumerFactory());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, FraudEvent> FraudEventConsumerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "alert-service-group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config,
                new StringDeserializer(),
                new JsonDeserializer<>(FraudEvent.class));

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FraudEvent> kafkaFraudListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FraudEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(FraudEventConsumerFactory());

        return factory;
    }

}
