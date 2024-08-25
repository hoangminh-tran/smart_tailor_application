package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.CustomerService;
import com.smart.tailor.utils.request.CustomerRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.CustomerAPI.CUSTOMER)
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final ObjectMapper objectMapper;

    @PutMapping(APIConstant.CustomerAPI.UPDATE_CUSTOMER_PROFILE + "/{customerID}")
    public ResponseEntity<ObjectNode> updateCustomerProfile(@PathVariable("customerID") String customerID,
                                                            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                            @Valid @RequestBody CustomerRequest customerRequest) {
        ObjectNode response = objectMapper.createObjectNode();
        var apiResponse = customerService.updateCustomerProfile(jwtToken, customerID, customerRequest);

        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }
}
