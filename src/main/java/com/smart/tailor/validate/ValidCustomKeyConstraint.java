package com.smart.tailor.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ValidCustomKeyConstraint implements ConstraintValidator<ValidCustomKey, String> {
    @Override
    public void initialize(ValidCustomKey constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9]{2}" +     // 2 ký tự ngẫu nhiên từ chars
                        "\\d{2}" +              // HH
                        "[a-zA-Z0-9]{2}" +      // 2 ký tự ngẫu nhiên từ chars
                        "[0-5][0-9]" +          // mm (00-59)
                        "[a-zA-Z0-9]{2}" +      // 2 ký tự ngẫu nhiên từ chars
                        "[0-5][0-9]" +          // ss (00-59)
                        "[0-9]{2}" +            // 2 ký tự từ microseconds, bất kỳ
                        "$"
        );
        return pattern.matcher(value).matches();
    }
}
