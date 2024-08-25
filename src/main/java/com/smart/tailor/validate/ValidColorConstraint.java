package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidColorConstraint implements ConstraintValidator<ValidColor, String> {
    @Override
    public boolean isValid(String color, ConstraintValidatorContext context) {
        if (color == null) {
            return true;
        }
        return color.length() <= 10;
    }
}