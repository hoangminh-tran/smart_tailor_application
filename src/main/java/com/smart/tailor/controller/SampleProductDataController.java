package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.SampleProductDataAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.service.SampleProductDataService;
import com.smart.tailor.utils.request.SampleProductDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(SampleProductDataAPI.SAMPLE_PRODUCT_DATA)
@Validated
public class SampleProductDataController {
    private final SampleProductDataService dataService;
    private final OrderService orderService;

    @PostMapping(SampleProductDataAPI.ADD_SAMPLE_PRODUCT_DATA)
    public ResponseEntity<ObjectNode> addSampleProductData(@Valid @RequestBody SampleProductDataRequest sampleData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        dataService.addNewSampleProductData(sampleData);
        var order = orderService.getOrderById(sampleData.getOrderID()).get();
        order.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
        orderService.updateOrder(
                order
        );
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.ADD_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @PutMapping(SampleProductDataAPI.UPDATE_SAMPLE_PRODUCT_DATA + "/{sampleModelID}")
    public ResponseEntity<ObjectNode> updateSampleProductData(@PathVariable("sampleModelID") String sampleModelID,
                                                              @Valid @RequestBody SampleProductDataRequest sampleData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        dataService.updateSampleProductData(sampleModelID, sampleData);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.UPDATE_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_DATA_BY_PARENT_ORDER_ID + "/{parentOrderID}")
    public ResponseEntity<ObjectNode> getSampleProductDataByParentOrderID(@PathVariable("parentOrderID") String parentOrderID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sampleProductDataResponses = dataService.getSampleProductDataByParentOrderID(parentOrderID);
        if (sampleProductDataResponses.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SAMPLE_PRODUCT_DATA);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sampleProductDataResponses));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_DATA_BY_PARENT_ORDER_ID + "/{parentOrderID}/{stageID}")
    public ResponseEntity<ObjectNode> getSampleProductDataByParentOrderIDAndStageID(@PathVariable("parentOrderID") String parentOrderID, @PathVariable("stageID") String stageID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sampleProductDataResponses = dataService.getSampleProductDataByParentOrderIDAndStageID(parentOrderID, stageID);
        if (sampleProductDataResponses.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SAMPLE_PRODUCT_DATA);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sampleProductDataResponses));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(SampleProductDataAPI.GET_SAMPLE_PRODUCT_DATA_BY_ID + "/{sampleModelID}")
    public ResponseEntity<ObjectNode> getSampleProductDataByID(@PathVariable("sampleModelID") String sampleModelID) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var sampleProductDataResponses = dataService.getSampleProductDataByID(sampleModelID);
        if (sampleProductDataResponses == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", MessageConstant.CAN_NOT_FIND_ANY_SAMPLE_PRODUCT_DATA);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("message", MessageConstant.GET_SAMPLE_PRODUCT_DATA_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(sampleProductDataResponses));
        }
        return ResponseEntity.ok(response);
    }
}
