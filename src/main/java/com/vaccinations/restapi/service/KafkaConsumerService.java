package com.vaccinations.restapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinations.restapi.dto.ReservationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public KafkaConsumerService(ObjectMapper objectMapper, MailService mailService) {
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "reservation-topic")
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            ReservationDTO response = objectMapper.readValue(consumerRecord.value(), ReservationDTO.class);
            log.info("Message Received: {}", response.toString());
             if(response.getVaccinationDTO() != null) {
                 mailService.sendMail(mailService.createRegistrationConfirmMail(response));
             } else {
                 mailService.sendMail(mailService.createRegistrationRefusalMail(response.getEmail()));
             }
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
