package com.vaccinations.restapi.model;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestPropertySource("classpath:ValidationMessages.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PatientTest {

    @Autowired
    private Environment env;

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void validateAllBlank() {
        Patient patient = Patient.builder()
                .firstName("")
                .lastName("")
                .pesel("")
                .email("")
                .phoneNumber("")
                .build();

        Set<ConstraintViolation<Patient>> violationSet = validator.validate(patient);

        assertFalse(violationSet.isEmpty());

        assertEquals(5, violationSet.size());
    }

    @Test
    public void validateNullBlank() {
        Patient patient = Patient.builder()
                .firstName(null)
                .lastName(null)
                .pesel(null)
                .email(null)
                .phoneNumber(null)
                .build();

        Set<ConstraintViolation<Patient>> violationSet = validator.validate(patient);

        assertFalse(violationSet.isEmpty());

        assertEquals(5, violationSet.size());
    }

    @Test
    public void invalidPESEL() {
        Patient patient = Patient.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .pesel("00000000000")
                .email("covid19.vaccinations.2021@gmail.com")
                .phoneNumber("+48123456789")
                .build();

        Set<ConstraintViolation<Patient>> violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        ConstraintViolation<Patient> violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.pesel.PESEL.message"), violation.getMessage());

        patient.setPesel(null);
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.pesel.PESEL.message"), violation.getMessage());

        patient.setPesel("630508");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.pesel.PESEL.message"), violation.getMessage());

        patient.setPesel("-----------");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.pesel.PESEL.message"), violation.getMessage());

        patient.setPesel("           ");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.pesel.PESEL.message"), violation.getMessage());
    }

    @Test
    public void invalidPhoneNumber() {
        Patient patient = Patient.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .pesel("78063052412")
                .email("covid19.vaccinations.2021@gmail.com")
                .phoneNumber(null)
                .build();

        Set<ConstraintViolation<Patient>> violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        ConstraintViolation<Patient> violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.phoneNumber.NotNull.message"), violation.getMessage());

        patient.setPhoneNumber("");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.phoneNumber.Pattern.message"), violation.getMessage());

        patient.setPhoneNumber("         ");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.phoneNumber.Pattern.message"), violation.getMessage());

        patient.setPhoneNumber("+481234567899");
        violationSet = validator.validate(patient);
        assertEquals(1, violationSet.size());
        violation = violationSet.iterator().next();
        assertEquals(env.getProperty("validation.phoneNumber.Pattern.message"), violation.getMessage());
    }
}
