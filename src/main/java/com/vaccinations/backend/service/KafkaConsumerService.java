package com.vaccinations.backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaccinations.backend.dto.ReservationDTO;
import com.vaccinations.backend.model.Patient;
import com.vaccinations.backend.model.Vaccination;
import com.vaccinations.backend.repository.VaccinationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducerService;
    private final VaccinationRepository vaccinationRepository;

    public KafkaConsumerService(ObjectMapper objectMapper, KafkaProducerService kafkaProducerService, VaccinationRepository vaccinationRepository) {
        this.objectMapper = objectMapper;
        this.kafkaProducerService = kafkaProducerService;
        this.vaccinationRepository = vaccinationRepository;
    }

    @KafkaListener(topics = "registration-topic")
    public void onMessage(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment) {
        try {
            Patient patient = objectMapper.readValue(consumerRecord.value(), Patient.class);
            log.info(patient.toString());

            Vaccination vaccination = vaccinationRepository.findFirstAvailableVaccinationDate();

            if (vaccination != null) {
                vaccinationRepository.save(updateVaccination(vaccination, patient));

                kafkaProducerService.sendReservation(ReservationDTO.map(patient, vaccination));
                log.info("Registration successfully completed: " + vaccination);
            } else {
                log.info("Completely booked: " + patient);
                kafkaProducerService.sendReservation(ReservationDTO.map(patient, null));
            }

            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private Vaccination updateVaccination(Vaccination vaccination, Patient patient) {
        List<Patient> patientList = vaccination.getPatients();
        patientList.add(patient);
        vaccination.setPatients(patientList);

        return vaccination;
    }

    private Vaccination getReservationData(Vaccination vaccination, Patient patient) {
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);
        vaccination.setPatients(patientList);

        return vaccination;
    }
}
