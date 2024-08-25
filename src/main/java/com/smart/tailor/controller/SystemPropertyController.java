package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.SystemPropertyAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.utils.request.SystemPropertiesRequest;
import com.smart.tailor.utils.request.SystemPropertiesUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(SystemPropertyAPI.SYSTEM_PROPERTY)
@Validated
public class SystemPropertyController {
    private final SystemPropertiesService systemService;

    @GetMapping(SystemPropertyAPI.GET_ALL_SYSTEM_PROPERTY)
    public ResponseEntity<ObjectNode> getAllSystemProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = systemService.getAll();
        response.put("status", 200);
        response.put("message", MessageConstant.GET_ALL_SYSTEM_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(SystemPropertyAPI.GET_ALL_SYSTEM_PROPERTY_BY_TYPE)
    public ResponseEntity<ObjectNode> getAllByType(@RequestParam("type") String type) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = systemService.getAllByPropertyType(type);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_SYSTEM_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(SystemPropertyAPI.GET_SYSTEM_PROPERTY + "/{systemID}")
    public ResponseEntity<ObjectNode> getByID(@PathVariable("systemID") String systemID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = systemService.getByID(systemID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_SYSTEM_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @PostMapping(SystemPropertyAPI.ADD_NEW_SYSTEM_PROPERTY)
    public ResponseEntity<ObjectNode> addNew(@Valid @RequestBody SystemPropertiesRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = systemService.addNewSystemProperty(request);
        response.put("status", 200);
        response.put("message", MessageConstant.ADD_NEW_SYSTEM_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @PutMapping(SystemPropertyAPI.UPDATE_SYSTEM_PROPERTY)
    public ResponseEntity<ObjectNode> addNew(@Valid @RequestBody List<SystemPropertiesUpdateRequest> request) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = systemService.updateSystemProperty(request);
        response.put("status", 200);
        response.put("message", MessageConstant.UPDATE_SYSTEM_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }
}
