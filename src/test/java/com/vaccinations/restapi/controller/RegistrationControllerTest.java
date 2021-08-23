package com.vaccinations.restapi.controller;

import com.vaccinations.restapi.model.ErrorResponse;
import com.vaccinations.restapi.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("classpath:ValidationMessages.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Environment env;

    @Test
    void addSuccess() {

        Patient patient = Patient.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("covid19.vaccinations.2021@gmail.com")
                .pesel("78063052412")
                .phoneNumber("+48123456789")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

        ResponseEntity<Patient> responseEntity = restTemplate.exchange("/api/registration/add", HttpMethod.POST, request, Patient.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(patient, responseEntity.getBody());
    }

    @Test
    void addBadRequest() {
        System.out.println(env.toString());
        Patient patient = Patient.builder()
                .firstName("")
                .lastName("")
                .email("")
                .pesel("00000000000")
                .phoneNumber("+48123")
                .build();

        Map<String, String> errors = new HashMap<>();
        errors.put("firstName", env.getProperty("validation.firstName.NotBlank.message"));
        errors.put("lastName", env.getProperty("validation.lastName.NotBlank.message"));
        errors.put("pesel", env.getProperty("validation.pesel.PESEL.message"));
        errors.put("email", env.getProperty("validation.email.NotBlank.message"));
        errors.put("phoneNumber", env.getProperty("validation.phoneNumber.Pattern.message"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/api/registration/add", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(errors, Objects.requireNonNull(responseEntity.getBody()).getErrors());
    }
}
