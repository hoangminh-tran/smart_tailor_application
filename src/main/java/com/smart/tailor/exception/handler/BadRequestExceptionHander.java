package com.smart.tailor.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.exception.BadRequestWithCustomStatusCodeException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@RequiredArgsConstructor
public class BadRequestExceptionHander {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ObjectNode> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode errorsArray = objectMapper.createArrayNode();

        e.getBindingResult().getAllErrors().forEach(error -> {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("field", ((FieldError) error).getField());
            errorNode.put("message", error.getDefaultMessage());
            errorsArray.add(errorNode);
        });

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation Error");
        response.set("errors", objectMapper.valueToTree(errorsArray));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ObjectNode> handleConstraintViolationException(ConstraintViolationException e) {
        ObjectNode response = objectMapper.createObjectNode();
        ArrayNode errorsArray = objectMapper.createArrayNode();

        e.getConstraintViolations().forEach(error -> {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("field", ((FieldError) error).getField());
            errorNode.put("message", error.getPropertyPath().toString());
            errorsArray.add(errorNode);
        });

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation Error");
        response.set("errors", objectMapper.valueToTree(errorsArray));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ObjectNode> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", MessageConstant.INVALID_INPUT);
        response.put("errors", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadRequestWithCustomStatusCodeException.class)
    public ResponseEntity<ObjectNode> handleBadRequestWithCustomStatusCodeException(BadRequestWithCustomStatusCodeException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", ex.getStatusCode().value());
        response.put("message", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode().value()).body(response);
    }
}
