package com.vaccinations.restapi.controller;

import com.vaccinations.restapi.model.ErrorResponse;
import com.vaccinations.restapi.model.Patient;
import com.vaccinations.restapi.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final KafkaProducerService producerService;

    public RegistrationController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Patient> add(@Valid @RequestBody Patient patient) {
        try {
            producerService.sendRegistration(patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(patient);
        } catch (Exception ex) {
            log.error("Endpoint exception /add: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Bad request - Not valid")
                .errors(errors)
                .build();
    }
}
