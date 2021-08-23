package com.vaccinations.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
