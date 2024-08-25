package com.smart.tailor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RestAccessDenyEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);

        objectNode.put("timestamp", timestamp);
        objectNode.put("status", HttpStatus.FORBIDDEN.value());
        objectNode.put("message", "You do not have permission to access this resource.");
        objectNode.put("path", request.getRequestURI());

        OutputStream outputStream = response.getOutputStream();
        objectMapper.writeValue(outputStream, objectNode);
        outputStream.flush();
    }
}