package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Stream;

public class ValidEnumValueConstraint implements ConstraintValidator<ValidEnumValue, CharSequence> {
    private List<String> acceptValues;

    @Override
    public void initialize(ValidEnumValue validEnumValue) {
        acceptValues = Stream.of(validEnumValue.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return acceptValues.contains(value.toString().toUpperCase());
    }
}