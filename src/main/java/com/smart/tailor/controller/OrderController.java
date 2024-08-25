package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.smart.tailor.constant.APIConstant.OrderAPI;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.event.CreateOrderEvent;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.RatingOrderRequest;
import com.smart.tailor.utils.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping(OrderAPI.ORDER)
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(OrderAPI.CREATE_ORDER)
    public ResponseEntity<ObjectNode> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CREATE_ORDER_SUCCESSFULLY);
            OrderResponse orderResponse = orderService.createOrder(orderRequest);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderByID(@PathVariable("orderID") String orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderByOrderID(orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getOrderByBrandID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                        @PathVariable("brandID") String brandID) throws Exception {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", 200);
        response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
        var orderResponse = orderService.getOrderByBrandID(jwtToken, brandID);
        response.set("data", objectMapper.valueToTree(orderResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_ORDER_BY_USER_ID + "/{userID}")
    public ResponseEntity<ObjectNode> getOrderByUserID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                       @PathVariable("userID") String userID) throws Exception {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", 200);
        response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
        var orderResponse = orderService.getOrderByUserID(jwtToken, userID);
        response.set("data", objectMapper.valueToTree(orderResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_ALL_ORDER)
    public ResponseEntity<ObjectNode> getAllOrder() {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getAllOrder();
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_PARENT_ORDER_BY_DESIGN_ID + "/{designID}")
    public ResponseEntity<ObjectNode> getParentOrderByDesignID(@PathVariable("designID") String designID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getParentOrderByDesignID(designID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ALL_ORDER + "/{parentID}")
    public ResponseEntity<ObjectNode> getAllSubOrderByParentOrderID(@PathVariable("parentID") String parentID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getSubOrderByParentID(parentID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @PutMapping(OrderAPI.CHANGE_STATUS_ORDER)
    public ResponseEntity<ObjectNode> changeOrderStatus(@Valid @RequestBody OrderStatusUpdateRequest orderRequest) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.CHANGE_ORDER_STATUS_SUCCESSFULLY);
            var orderResponse = orderService.changeOrderStatus(orderRequest);
            response.set("data", objectMapper.valueToTree(orderResponse));
            if (orderResponse.getOrderType().contains("PARENT_ORDER") &&
                    orderResponse.getOrderStatus().equals(OrderStatus.PENDING)) {
                applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));
            }

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @PostMapping(OrderAPI.BRAND_PICK_ORDER)
    public ResponseEntity<ObjectNode> pickOrder(@RequestBody OrderPickingRequest orderPicking) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            if (orderPicking == null) {
                response.put("status", 400);
                response.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(response);
            }

            var brandPicked = orderService.brandPickOrder(orderPicking);
            response.put("status", 200);
            response.put("message", MessageConstant.BRAND_PICK_ORDER_SUCCESSFULLY);
            response.set("data", objectMapper.valueToTree(brandPicked));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", -1);
            response.put("message", ex.getMessage());
            logger.error("ERROR IN BRAND PICKING ORDER. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_DETAIL_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderDetailByID(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                         @PathVariable("orderID") String orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getOrderDetailByOrderID(jwtToken, orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            if (ex instanceof ResponseStatusException) {
                return new ResponseEntity<>(((ResponseStatusException) ex).getStatusCode());
            }
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_FULL_PROP)
    public ResponseEntity<ObjectNode> getFullPropOrder() {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getFullProp();
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_FULL_PROP_BY_BRAND_ID + "/{brandID}")
    public ResponseEntity<ObjectNode> getFullPropOrderByBrandID(@PathVariable("brandID") String brandID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_SUCCESSFULLY);
            var orderResponse = orderService.getFullPropByBrandID(brandID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping(OrderAPI.GET_ORDER_STAGE_BY_ID + "/{orderID}")
    public ResponseEntity<ObjectNode> getOrderStageByID(@PathVariable("orderID") String orderID) {
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("status", 200);
            response.put("message", MessageConstant.GET_ORDER_STAGE_SUCCESSFULLY);
            var orderResponse = orderService.getOrderStageByOrderID(orderID);
            response.set("data", objectMapper.valueToTree(orderResponse));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("ERROR IN ORDER CONTROLLER: {}", ex.getMessage());
            return null;
        }
    }

    @GetMapping("/filter-brand-by-design-id/{designID}")
    public ResponseEntity<ObjectNode> filterBrandForSpecificOrderBaseOnDesign(@PathVariable("designID") String designID) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", 200);
        response.put("message", "Filter Brand For Specific Order Base On Design");
        var listFilterBrand = orderService.filterBrandForSpecificOrderBaseOnDesign(designID);
        response.set("data", objectMapper.valueToTree(listFilterBrand));
        return ResponseEntity.ok(response);
    }

    @PostMapping(OrderAPI.RATING_ORDER)
    public ResponseEntity<ObjectNode> ratingOrder(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                                  @Valid @RequestBody RatingOrderRequest ratingOrderRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        orderService.ratingOrder(jwtToken, ratingOrderRequest);
        response.put("status", HttpStatus.OK.value());
        response.put("message", MessageConstant.RATING_ORDER_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.ORDER_TIME_LINE_BY_PARENT_ORDER_ID + "/{parentOrderID}")
    public ResponseEntity<ObjectNode> orderTimeLineByParentOrderID(@PathVariable("parentOrderID") String parentOrderID) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Order Time Line by Parent Order ID Successfully");
        response.set("data", objectMapper.valueToTree(orderService.getOrderTimeLineByParentOrderID(parentOrderID)));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.ORDER_TIME_LINE_BY_SUB_ORDER_ID + "/{subOrderID}")
    public ResponseEntity<ObjectNode> orderTimeLineSubOrderID(@PathVariable("subOrderID") String subOrderID) {
        ObjectNode response = objectMapper.createObjectNode();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Order Time Line by Sub Order ID Successfully");
        response.set("data", objectMapper.valueToTree(orderService.getOrderTimeLineBySubOrderID(subOrderID)));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_ORDER_SHIPPING_DETAIL_BY_LABEL_ID + "/{labelID}")
    public ResponseEntity<ObjectNode> getOrderDetailShippingResponseByLabelID(@PathVariable("labelID") String labelID) {
        ObjectNode response = objectMapper.createObjectNode();
        var orderDetailShippingResponse = orderService.getOrderDetailShippingResponseByLabelID(labelID);
        if (orderDetailShippingResponse.isSuccess()) {
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Get Order Detail Shipping Response By Label ID Successfully");
            response.set("data", objectMapper.valueToTree(orderDetailShippingResponse));
        } else {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", orderDetailShippingResponse.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_ORDER_STATUS_DETAIL)
    public ResponseEntity<ObjectNode> getOrderStatusDetail() {
        ObjectNode response = objectMapper.createObjectNode();
        var orderDetailShippingResponse = orderService.getAllOrderStatusDetailResponse();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Order Status Detail Successfully");
        response.set("data", objectMapper.valueToTree(orderDetailShippingResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.CALCULATE_ORDER_GROWTH_PERCENTAGE_FOR_CURRENT_AND_PREVIOUS_MONTH)
    public ResponseEntity<ObjectNode> calculateOrderGrowthPercentageForCurrentAndPreviousMonth() {
        ObjectNode response = objectMapper.createObjectNode();
        var growthResponse = orderService.calculateOrderGrowthPercentageForCurrentAndPreviousMonth();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Calculate Order Growth Percentage For Current and Previous Month Successfully");
        response.set("data", objectMapper.valueToTree(growthResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_TOTAL_ORDER_OF_EACH_BRAND)
    public ResponseEntity<ObjectNode> getTotalOrderOfEachBrand() {
        ObjectNode response = objectMapper.createObjectNode();
        var orderDetailShippingResponse = orderService.getAllTotalOrderOfEachBrand();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Total Order Of Each Brand Successfully");
        response.set("data", objectMapper.valueToTree(orderDetailShippingResponse));
        return ResponseEntity.ok(response);
    }

    @GetMapping(OrderAPI.GET_SUB_ORDER_INVOICE_BY_SUB_ORDER_ID + "/{subOrderID}")
    public ResponseEntity<ObjectNode> getSubOrderInvoiceBySubOrderID(@PathVariable("subOrderID") String subOrderID) throws Exception {
        ObjectNode response = objectMapper.createObjectNode();
        var orderDetailShippingResponse = orderService.getSubOrderInvoiceBySubOrderID(subOrderID);
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Get Sub Order Invoice By SubOrderID Successfully");
        response.set("data", objectMapper.valueToTree(orderDetailShippingResponse));
        return ResponseEntity.ok(response);
    }
}
