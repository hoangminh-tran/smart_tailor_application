package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.PaymentAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.service.PayOSService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.utils.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentAPI.PAYMENT)
@Validated
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;
    private final PayOSService payOSService;

    @GetMapping(PaymentAPI.PAYMENT_INFO + "/{paymentID}")
    ResponseEntity<ObjectNode> getPaymentInfo(@PathVariable("paymentID") String paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var data = paymentService.getPaymentByID(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(data));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN PAYMENT CONTROLLER: {}", ex.getMessage());
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.CONFIRM_PAYMENT + "/{paymentID}")
    ResponseEntity<ObjectNode> confirmPayment(@PathVariable("paymentID") Integer paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            payOSService.confirmPayment(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.CONFIRM_PAYMENT_SUCCESSFULLY);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN PAYMENT CONTROLLER: {}", ex.getMessage());
            throw ex;
        }
    }

    @PostMapping(PaymentAPI.CREATE_PAYMENT)
    ResponseEntity<ObjectNode> createPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.createPayOSPayment(paymentRequest);
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR WHEN CREATE MANUAL PAYMENT!", ex.getMessage());
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.MANUAL_PAYMENT_INFO + "/{paymentID}")
    ResponseEntity<ObjectNode> getManualPaymentByID(@PathVariable("paymentID") String paymentID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getManualPaymentByID(paymentID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.GET_PAYMENT_BY_USER_ID + "/{userID}")
    ResponseEntity<ObjectNode> getPaymentByUserID(@PathVariable("userID") String userID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getPaymentByUserID(userID);
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.GET_ALL_PAYMENT)
    ResponseEntity<ObjectNode> getAllPayment() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            var value = paymentService.getAllPaymentResponse();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_PAYMENT_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(value));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping(PaymentAPI.CALCULATE_PAYMENT_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK)
    ResponseEntity<ObjectNode> calculatePaymentGrowthPercentageForCurrentAndPreviousWeek(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthPercentageResponse = paymentService.calculatePaymentGrowthPercentageForCurrentAndPreviousWeek();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate Payment Growth Percentage For Current and Previous Week Successfully");
        response.set("data", objectMapper.valueToTree(growthPercentageResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(PaymentAPI.CALCULATE_INCOME_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_WEEK)
    ResponseEntity<ObjectNode> calculateIncomeGrowthPercentageForCurrentAndPreviousWeek(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthPercentageResponse = paymentService.calculateIncomeGrowthPercentageForCurrentAndPreviousWeek();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate Income Growth Percentage For Current and Previous Week Successfully");
        response.set("data", objectMapper.valueToTree(growthPercentageResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(PaymentAPI.CALCULATE_REFUND_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_MONTH)
    ResponseEntity<ObjectNode> calculateRefundGrowthPercentageForCurrentAndPreviousMonth(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var growthPercentageResponse = paymentService.calculateRefundGrowthPercentageForCurrentAndPreviousMonth();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate Refund Growth Percentage For Current and Previous Month Successfully");
        response.set("data", objectMapper.valueToTree(growthPercentageResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(PaymentAPI.GET_TOTAL_PAYMENT_OF_EACH_MONTH)
    ResponseEntity<ObjectNode> getTotalPaymentOfEachMonth(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var totalPaymentOfEachMonth = paymentService.getTotalPaymentOfEachMonth();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Total Payment of Each Month Successfully");
        response.set("data", objectMapper.valueToTree(totalPaymentOfEachMonth));
        return ResponseEntity.ok(response);
    }

    @GetMapping(PaymentAPI.GET_TOTAL_INCOME_OF_EACH_MONTH)
    ResponseEntity<ObjectNode> getTotalIncomePaymentOfEachMonth(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var totalIncomePaymentOfEachMonth = paymentService.getTotalIncomePaymentOfEachMonth();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Total Income of Each Month Successfully");
        response.set("data", objectMapper.valueToTree(totalIncomePaymentOfEachMonth));
        return ResponseEntity.ok(response);
    }

    @GetMapping(PaymentAPI.GET_TOTAL_REFUND_OF_EACH_MONTH)
    ResponseEntity<ObjectNode> getTotalRefundPaymentOfEachMonth(){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        var totalRefundPaymentOfEachMonth = paymentService.getTotalRefundPaymentOfEachMonth();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Total Refund of Each Month Successfully");
        response.set("data", objectMapper.valueToTree(totalRefundPaymentOfEachMonth));
        return ResponseEntity.ok(response);
    }
}
