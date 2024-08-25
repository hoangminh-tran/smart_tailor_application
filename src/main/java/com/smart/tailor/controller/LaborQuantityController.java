package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.request.LaborQuantityRequestList;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstant.LaborQuantityAPI.LABOR_QUANTITY)
@RequiredArgsConstructor
@Slf4j
@Validated
public class LaborQuantityController {
    private final LaborQuantityService laborQuantityService;
    private final Logger logger = LoggerFactory.getLogger(LaborQuantityController.class);

    @GetMapping(APIConstant.LaborQuantityAPI.GET_ALL_LABOR_QUANTITY)
    public ResponseEntity<ObjectNode> getAllLaborQuantity() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var laborQuantityResponseList = laborQuantityService.findAllLaborQuantity();
        if (!laborQuantityResponseList.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_ALL_LABOR_QUANTITY_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(laborQuantityResponseList));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_LABOR_QUANTITY);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(APIConstant.LaborQuantityAPI.ADD_NEW_LABOR_QUANTITY)
    public ResponseEntity<ObjectNode> addNewLaborQuantity(@Valid @RequestBody LaborQuantityRequestList laborQuantityRequestList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        laborQuantityService.createLaborQuantity(laborQuantityRequestList);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_LABOR_QUANTITY_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(APIConstant.LaborQuantityAPI.UPDATE_LABOR_QUANTITY + "/{laborQuantityID}")
    public ResponseEntity<ObjectNode> updateLaborQuantity( @PathVariable("laborQuantityID") String laborQuantityID,
                                                          @Valid @RequestBody LaborQuantityRequest laborQuantityRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        laborQuantityService.updateLaborQuantity(laborQuantityID, laborQuantityRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_LABOR_QUANTITY_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}
