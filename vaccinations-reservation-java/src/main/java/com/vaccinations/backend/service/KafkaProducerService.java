package com.vaccinations.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinations.backend.dto.ReservationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendReservation(ReservationDTO reservationDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(reservationDTO);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("reservation-topic", message);
        future.addCallback(new KafkaSendCallback<>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                handleSuccess(message, result);
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                handleFailure(message, ex);
            }
        });
    }

    private void handleFailure(String value, Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in onFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String value, SendResult<String, String> result) {
        log.info("Message Sent: {}, partition is {}", value, result.getRecordMetadata().partition());
    }
}
