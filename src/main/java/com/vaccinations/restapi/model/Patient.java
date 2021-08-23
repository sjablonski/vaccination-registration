package com.vaccinations.restapi.model;

import com.vaccinations.restapi.validator.PESEL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String id;

    @NotBlank(message = "{validation.firstName.NotBlank.message}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.NotBlank.message}")
    private String lastName;

    @PESEL(message = "{validation.pesel.PESEL.message}")
    private String pesel;

    @NotBlank(message = "{validation.email.NotBlank.message}")
    @Email(message = "{validation.email.Email.message}")
    private String email;

    @NotNull(message = "{validation.phoneNumber.NotNull.message}")
    @Pattern(regexp = "^(?:(?:(?:(?:\\+|00)\\d{2})?[ -]?(?:(?:\\(0?\\d{2}\\))|(?:0?\\d{2})))?[ -]?(?:\\d{3}[- ]?\\d{2}[- ]?\\d{2}|\\d{2}[- ]?\\d{2}[- ]?\\d{3}|\\d{7})|(?:(?:(?:\\+|00)\\d{2})?[ -]?\\d{3}[ -]?\\d{3}[ -]?\\d{3}))$",
            message = "{validation.phoneNumber.Pattern.message}")
    private String phoneNumber;

    @Override
    public String toString() {
        return String.format("Patient [id=\"%s\", firstName=\"%s\", lastName=\"%s\", pesel=\"%s\", email=\"%s\", phoneNumber=\"%s\"]", id, firstName, lastName, pesel, email, phoneNumber);
    }
}
