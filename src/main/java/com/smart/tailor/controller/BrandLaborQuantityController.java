package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.BrandLaborQuantityService;
import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.request.BrandLaborQuantityRequest;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstant.BrandLaborQuantityAPI.BRAND_LABOR_QUANTITY)
@RequiredArgsConstructor
@Slf4j
@Validated
public class BrandLaborQuantityController {
    private final BrandLaborQuantityService brandLaborQuantityService;
    private final Logger logger = LoggerFactory.getLogger(BrandLaborQuantityController.class);

    @GetMapping(APIConstant.BrandLaborQuantityAPI.GET_ALL_BRAND_LABOR_QUANTITY_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getAllLaborQuantityByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                                   @PathVariable("brandID") String brandID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var brandLaborQuantityResponses = brandLaborQuantityService.findBrandLaborQuantityByBrandID(jwtToken, brandID);
        if (!brandLaborQuantityResponses.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_BRAND_LABOR_QUANTITY_BY_BRAND_ID_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandLaborQuantityResponses));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_BRAND_LABOR_QUANTITY_BY_BRAND_ID);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.BrandLaborQuantityAPI.ADD_NEW_BRAND_LABOR_QUANTITY)
    public ResponseEntity<ObjectNode> addNewBrandLaborQuantity(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                               @Valid @RequestBody BrandLaborQuantityListRequest brandLaborQuantityListRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        brandLaborQuantityService.createBrandLaborQuantity(jwtToken, brandLaborQuantityListRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_BRAND_LABOR_QUANTITY_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.BrandLaborQuantityAPI.UPDATE_BRAND_LABOR_QUANTITY + "/{brandID}")
    public ResponseEntity<ObjectNode> updateLaborQuantity(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                          @PathVariable("brandID") String brandID,
                                                          @Valid @RequestBody BrandLaborQuantityRequest brandLaborQuantityRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        brandLaborQuantityService.updateBrandLaborQuantity(jwtToken, brandID, brandLaborQuantityRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_LABOR_QUANTITY_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
