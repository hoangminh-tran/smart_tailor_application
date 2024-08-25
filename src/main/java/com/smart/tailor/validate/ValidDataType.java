package com.smart.tailor.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE, TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDataTypeConstraint.class)
public @interface ValidDataType {
    String message() default "Invalid Data Type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
