package com.vaccinations.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "vaccinationSchedule")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vaccination {

    @Id
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
