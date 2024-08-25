package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelFileDuplicateDataException extends RuntimeException {
    private List<Object> errors;

    public ExcelFileDuplicateDataException(String message, List<Object> errors) {
        super(message);
        this.errors = errors;
    }
}
