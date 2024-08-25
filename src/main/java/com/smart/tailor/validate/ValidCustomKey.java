package com.smart.tailor.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;

@Documented
@Constraint(validatedBy = ValidCustomKeyConstraint.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomKey {
    String message() default "Invalid Custom Primary Key";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}