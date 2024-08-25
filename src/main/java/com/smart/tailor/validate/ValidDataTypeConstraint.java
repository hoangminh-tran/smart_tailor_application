package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDataTypeConstraint implements ConstraintValidator<ValidDataType, Object> {
    @Override
    public void initialize(ValidDataType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof Boolean) {
            return true;
        } else if (value instanceof Double) {
            return true;
        } else if (value instanceof Float) {
            return true;
        } else if (value instanceof Integer) {
            return true;
        }

        return false;
    }
}
