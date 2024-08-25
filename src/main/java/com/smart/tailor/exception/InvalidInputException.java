package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Getter
@Setter
public class InvalidInputException extends ResponseStatusException {
    private HashMap errorFields;

    public InvalidInputException(HttpStatusCode statusCode, String message, HashMap errorFields) {
        super(statusCode, message);
        this.errorFields = errorFields;
    }
}
