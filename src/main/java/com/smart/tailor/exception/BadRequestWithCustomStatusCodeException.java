package com.smart.tailor.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
@Setter
public class BadRequestWithCustomStatusCodeException extends ResponseStatusException {
    public BadRequestWithCustomStatusCodeException(HttpStatusCode statusCode, String message) {
        super(statusCode, message);
    }
}
