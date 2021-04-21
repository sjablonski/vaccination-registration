package com.vaccinations.backend.dto;

import com.vaccinations.backend.model.Patient;
import com.vaccinations.backend.model.Vaccination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private String firstName;
    private String lastName;
    private String pesel;
    private String email;
    private String phoneNumber;
    private VaccinationDTO vaccinationDTO;

    public static ReservationDTO map(Patient patient, Vaccination vaccination) {
        return ReservationDTO.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .pesel(patient.getPesel())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .vaccinationDTO(VaccinationDTO.map(vaccination))
                .build();
    }
}
