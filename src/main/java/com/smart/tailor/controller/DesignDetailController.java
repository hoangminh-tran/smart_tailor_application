package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.DesignDetailAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.DesignDetailService;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.validate.ValidCustomKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(DesignDetailAPI.DESIGN_DETAIL)
@RequiredArgsConstructor
@Validated
public class DesignDetailController {
    private final Logger logger = LoggerFactory.getLogger(DesignDetailController.class);
    private final ObjectMapper objectMapper;
    private final DesignDetailService designDetailService;

    @PostMapping(DesignDetailAPI.ADD_NEW_DESIGN_DETAIL)
    public ResponseEntity<ObjectNode> addNewDesignDetail(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                         @RequestBody DesignDetailRequest designDetailRequest) {
        var apiResponse = designDetailService.createDesignDetail(jwtToken, designDetailRequest);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", apiResponse.getStatus());
        response.put("message", apiResponse.getMessage());
        response.set("data", objectMapper.valueToTree(apiResponse.getData()));
        return ResponseEntity.ok(response);
    }

    @GetMapping(DesignDetailAPI.GET_ALL_DESIGN_DETAIL_BY_ORDER_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getAllDesignDetailByOrderID(@PathVariable("orderID") String orderID) {
        var designDetailResponseList = designDetailService.findAllByOrderID(orderID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GET_ALL_DESIGN_DETAIL_BY_ORDER_ID_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(designDetailResponseList));
        return ResponseEntity.ok(response);
    }

    @GetMapping(DesignDetailAPI.GET_DESIGN_DETAIL_BY_ID + "/{detailID}")
    public ResponseEntity<ObjectNode> getDesignDetailByDesignID( @PathVariable("detailID") String detailID) {
        var designDetailResponseList = designDetailService.findByID(detailID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.GET_DESIGN_DETAIL_BY_ID_SUCCESSFULLY);
        response.set("data", objectMapper.valueToTree(designDetailResponseList));
        return ResponseEntity.ok(response);
    }

    @GetMapping(DesignDetailAPI.CALCULATE_TOTAL_PRICE_BY_PARENT_ORDER_ID + "/{parentOrderID}")
    public ResponseEntity<ObjectNode> calculateTotalPriceForSpecificOrder( @PathVariable("parentOrderID") String parentOrderID) throws Exception {
        var totalPrice = designDetailService.calculateTotalPriceForSpecificOrder(parentOrderID);
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "calculateTotalPriceForSpecificOrder");
        response.set("data", objectMapper.valueToTree(totalPrice));
        return ResponseEntity.ok(response);
    }
}
