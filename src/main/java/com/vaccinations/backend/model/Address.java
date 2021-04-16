package com.vaccinations.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {

    public String institutionName;
    public String street;
    public String city;
    public String state;
    public String zip;

    @Override
    public String toString() {
        return String.format("Address [institutionName=\"%s\", street=\"%s\", city=\"%s\", state=\"%s\", zip=\"%s\"]", institutionName, street, city, state, zip);
    }
}
