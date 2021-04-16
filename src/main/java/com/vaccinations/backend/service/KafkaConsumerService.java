package com.vaccinations.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinations.backend.model.Patient;
import com.vaccinations.backend.model.Vaccination;
import com.vaccinations.backend.repository.VaccinationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final VaccinationRepository vaccinationRepository;
    private final MailService mailService;

    public KafkaConsumerService(VaccinationRepository vaccinationRepository, MailService mailService) {
        this.objectMapper = new ObjectMapper();
        this.vaccinationRepository = vaccinationRepository;
        this.mailService = mailService;
    }

    @KafkaListener(id = "test-topic", topics = "test-topic")
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            Patient patient = objectMapper.readValue(consumerRecord.value(), Patient.class);
            log.info(patient.toString());

            Vaccination vaccination = vaccinationRepository.findFirstAvailableVaccinationDate();

            if (vaccination != null) {
                List<Patient> patientList = vaccination.getPatients();
                patientList.add(patient);
                vaccination.setPatients(patientList);

                vaccinationRepository.save(vaccination);

                mailService.sendMail(mailService.createRegistrationConfirmMail(vaccination, patient));
                log.info("Registration successfully completed: " + vaccination);
            } else {
                log.info("Completely booked: " + patient);
                mailService.sendMail(mailService.createRegistrationRefusalMail(patient.getEmail()));
            }

            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
