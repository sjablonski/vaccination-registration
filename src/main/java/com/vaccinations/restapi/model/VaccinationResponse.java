package com.vaccinations.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationResponse {
    private Vaccination vaccination;
    private Patient patient;
}
