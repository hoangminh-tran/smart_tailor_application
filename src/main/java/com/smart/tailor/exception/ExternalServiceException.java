package com.smart.tailor.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ExternalServiceException extends ResponseStatusException {
    public ExternalServiceException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
