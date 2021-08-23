package com.vaccinations.restapi.dto;

import com.vaccinations.restapi.model.Patient;
import com.vaccinations.restapi.model.Vaccination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String pesel;
    private String email;
    private String phoneNumber;
    private VaccinationDTO vaccinationDTO;

    public static ReservationDTO map(Patient patient, Vaccination vaccination) {
        return ReservationDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .pesel(patient.getPesel())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .vaccinationDTO(VaccinationDTO.map(vaccination))
                .build();
    }
}
