package com.vaccinations.restapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinations.restapi.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendRegistration(Patient patient) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        String message = objectMapper.writeValueAsString(patient);

        try {
            SendResult<String, String> result = kafkaTemplate.send("registration-topic", message).get(10, TimeUnit.SECONDS);
            log.info("Message Sent: {}, partition is {}", message, result.getRecordMetadata().partition());
        } catch (Exception ex) {
            log.error("Exception Sending the Message and the exception is {}", ex.getMessage());
            throw ex;
        }
    }
}
