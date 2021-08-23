package com.vaccinations.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private String pesel;
    private String email;
    private String phoneNumber;

    @Override
    public String toString() {
        return String.format("Patient [id=\"%s\", firstName=\"%s\", lastName=\"%s\", pesel=\"%s\", email=\"%s\", phoneNumber=\"%s\"]", id, firstName, lastName, pesel, email, phoneNumber);
    }
}
