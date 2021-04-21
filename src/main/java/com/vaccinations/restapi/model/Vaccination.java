package com.vaccinations.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vaccination {

    private String id;
    private String date;
    private String time;
    private List<Patient> patients;
    private Address address;

    @Override
    public String toString() {
        return String.format("Vaccination [id=\"%s\", date=\"%s\", time=\"%s\", patients=%s, address=%s]", id, date, time, patients.toString(), address);
    }
}
