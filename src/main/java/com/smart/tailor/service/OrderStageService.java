package com.smart.tailor.service;

import com.smart.tailor.entities.OrderStage;
import com.smart.tailor.utils.request.OrderStageRequest;
import com.smart.tailor.utils.response.OrderStageResponse;

import java.util.List;


public interface OrderStageService {
    OrderStageResponse createOrderStage(OrderStageRequest orderStageRequest);

    List<OrderStageResponse> getOrderStageByOrderID(String orderID);

    OrderStage getOrderStageByID(String stageID);

    void updateStage(OrderStage orderStage);
}
