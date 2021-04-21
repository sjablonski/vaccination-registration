package com.vaccinations.backend.dto;

import com.vaccinations.backend.model.Address;
import com.vaccinations.backend.model.Vaccination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationDTO {

    private String date;
    private String time;
    private Address address;

    public static VaccinationDTO map(Vaccination vaccination) {
        if (vaccination == null) {
            return null;
        }
        return VaccinationDTO.builder()
                .date(vaccination.getDate())
                .time(vaccination.getTime())
                .address(vaccination.getAddress())
                .build();
    }
}
