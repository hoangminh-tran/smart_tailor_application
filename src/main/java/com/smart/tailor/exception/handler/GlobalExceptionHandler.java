package com.smart.tailor.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ObjectNode> handleBadRequestException(BadRequestException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ObjectNode> handleItemNotFoundException(ItemNotFoundException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ItemAlreadyExistException.class)
    public ResponseEntity<ObjectNode> handleItemNotFoundException(ItemAlreadyExistException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ObjectNode> handleExternalServiceException(ExternalServiceException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", ex.getStatusCode().value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ObjectNode> handleSystemException(Exception ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ObjectNode> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String unsupportedMethod = ex.getMethod();
        List<String> supportedMethod = new ArrayList<>();
        for (HttpMethod requestMethod : ex.getSupportedHttpMethods()) {
            supportedMethod.add(requestMethod.name());
        }
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        response.put("message", "Method " + unsupportedMethod + " is not supported for this endpoint.");
        response.set("supportedMethod", objectMapper.valueToTree(supportedMethod));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ObjectNode> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Malformed JSON request");
        response.put("errors", ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ObjectNode> handleDuplicateDataException(DuplicateDataException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());
        response.set("errors", objectMapper.valueToTree(ex.getErrors()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MultipleErrorException.class)
    public ResponseEntity<ObjectNode> handleMultipleErrorException(MultipleErrorException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", ex.getStatusCode().value());
        response.put("message", ex.getReason());
        response.set("errors", objectMapper.valueToTree(ex.getErrorDetails()));
        return ResponseEntity.status(ex.getStatusCode().value()).body(response);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ObjectNode> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        response.put("timestamp", timestamp);
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ObjectNode> handleIllegalArgumentException(IllegalArgumentException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        response.put("timestamp", timestamp);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
