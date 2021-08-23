package com.vaccinations.restapi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PESELValidator.class)
@Documented
public @interface PESEL {
    String message() default "PESEL not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}