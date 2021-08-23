package com.vaccinations.restapi.dto;

import com.vaccinations.restapi.model.Address;
import com.vaccinations.restapi.model.Vaccination;
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
        return VaccinationDTO.builder()
                .date(vaccination.getDate())
                .time(vaccination.getTime())
                .address(vaccination.getAddress())
                .build();
    }
}
