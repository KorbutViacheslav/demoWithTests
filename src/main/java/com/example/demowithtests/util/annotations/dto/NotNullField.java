package com.example.demowithtests.util.annotations.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotNullFieldValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullField {

    String message() default "Name, country and gender not be a null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
