package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.BrandPropertyAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.BrandPropertiesService;
import com.smart.tailor.utils.request.BrandPropertiesRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping(BrandPropertyAPI.BRAND_PROPERTY)
@Validated
public class BrandPropertyController {
    private final BrandPropertiesService brandPropertiesService;

    @GetMapping(BrandPropertyAPI.GET_ALL_BRAND_PROPERTY)
    public ResponseEntity<ObjectNode> getAllBrandProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getAll();
        response.put("status", 200);
        response.put("message", MessageConstant.GET_ALL_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandPropertyAPI.GET_ALL_BRAND_PROPERTY_BY_BRAND_ID)
    public ResponseEntity<ObjectNode> getAllByBrandID(@RequestParam("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getAllByBrandID(brandID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandPropertyAPI.GET_BRAND_PROPERTY + "/{systemID}")
    public ResponseEntity<ObjectNode> getByID(@PathVariable("systemID") String systemID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getByID(systemID);
        response.put("status", 200);
        response.put("message", MessageConstant.GET_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @PostMapping(BrandPropertyAPI.ADD_NEW_BRAND_PROPERTY)
    public ResponseEntity<ObjectNode> addNew(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                             @Valid @RequestBody BrandPropertiesRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.addNew(jwtToken, request);
        response.put("status", 200);
        response.put("message", MessageConstant.ADD_NEW_BRAND_PROPERTY_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @PostMapping(BrandPropertyAPI.UPDATE_BRAND_PROPERTY)
    public ResponseEntity<ObjectNode> updateBrandProperties(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                            @Valid @RequestBody BrandPropertiesRequest request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.updateBrandProperties(jwtToken, request);
        response.put("status", 200);
        response.put("message","Update Brand Properties Successfully");
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }

    @GetMapping(BrandPropertyAPI.GET_BRAND_PRODUCTIVITY_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getBrandProductivityByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                    @PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var data = brandPropertiesService.getBrandProductivityByBrandID(jwtToken, brandID);
        response.put("status", 200);
        response.put("message", "Get Brand Productivity Successfully");
        response.set("data", objectMapper.valueToTree(data));
        return ResponseEntity.ok(response);
    }
}
