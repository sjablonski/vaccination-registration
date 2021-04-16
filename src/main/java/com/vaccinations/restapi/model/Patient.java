package com.vaccinations.restapi.model;

import com.vaccinations.restapi.validator.PESEL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @NotBlank(message = "Not blank")
    private String firstName;

    @NotBlank(message = "Not blank")
    private String lastName;

    @PESEL
    private String pesel;

    @Email(message = "Not email")
    private String email;

    @NotBlank(message = "Not blank")
    @Pattern(regexp = "^(?:(?:(?:(?:\\+|00)\\d{2})?[ -]?(?:(?:\\(0?\\d{2}\\))|(?:0?\\d{2})))?[ -]?(?:\\d{3}[- ]?\\d{2}[- ]?\\d{2}|\\d{2}[- ]?\\d{2}[- ]?\\d{3}|\\d{7})|(?:(?:(?:\\+|00)\\d{2})?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}))$")
    private String phoneNumber;

    @Override
    public String toString() {
        return String.format("Patient [firstName=\"%s\", lastName=\"%s\", pesel=\"%s\", email=\"%s\", phoneNumber=\"%s\"]", firstName, lastName, pesel, email, phoneNumber);
    }
}
