package com.smart.tailor.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExcelExceptionHandler {
    private final ObjectMapper objectMapper;

    @ExceptionHandler(ExcelFileInvalidFormatException.class)
    public ResponseEntity<ObjectNode> handleExcelFileInvalidFormatException(ExcelFileInvalidFormatException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExcelFileNotSupportException.class)
    public ResponseEntity<ObjectNode> handleExcelFileNotSupportException(ExcelFileNotSupportException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    @ExceptionHandler(ExcelFileDuplicateDataException.class)
    public ResponseEntity<ObjectNode> handleExcelFileDuplicateDataException(ExcelFileDuplicateDataException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.set("errors", objectMapper.valueToTree(ex.getErrors()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExcelFileInvalidDataTypeException.class)
    public ResponseEntity<ObjectNode> handleExcelFileInvalidDataTypeException(ExcelFileInvalidDataTypeException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", ex.getMessage());
        response.set("errors", objectMapper.valueToTree(ex.getErrorFields()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExcelFileErrorReadingException.class)
    public ResponseEntity<ObjectNode> handleExcelFileErrorReadingException(ExcelFileErrorReadingException ex) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
