package com.vaccinations.restapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.replicas}")
    private int replicas;

    @Bean
    public NewTopic registrationTopic() {
        return TopicBuilder
                .name("registration-topic")
                .partitions(1)
                .replicas(replicas)
                .build();
    }

    @Bean
    public NewTopic reservationTopic() {
        return TopicBuilder
                .name("reservation-topic")
                .partitions(1)
                .replicas(replicas)
                .build();
    }
}
