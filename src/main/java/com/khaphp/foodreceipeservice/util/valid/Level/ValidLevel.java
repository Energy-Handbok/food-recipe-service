package com.khaphp.foodreceipeservice.util.valid.Level;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidLevelValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLevel {
    String message() default "Invalid Gender, must be EASY, MEDIUM, DIFFICULT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
