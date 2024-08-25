package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


@Setter
@Getter
public class MultipleErrorException extends ResponseStatusException {
    private Object errorDetails;

    public MultipleErrorException(HttpStatusCode statusCode, String message, Object errorDetails) {
        super(statusCode, message);
        this.errorDetails = errorDetails;
    }
}
