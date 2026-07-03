package org.example.empresa.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.empresa.constraint.validators.ValidNameValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ValidNameValidator.class)
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface ValidName {
    String message() default "El nombre no puede contener caracteres especiales";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
