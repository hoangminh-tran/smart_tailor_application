package com.smart.tailor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.RatingOrderRequest;
import com.smart.tailor.utils.response.*;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Optional;


public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest) throws Exception;

    List<OrderResponse> getParentOrderByDesignID(String designID) throws Exception;

    OrderCustomResponse getOrderByOrderID(String orderID) throws Exception;

    OrderCustomResponse getOrderDetailByOrderID(String jwtToken, String orderID) throws Exception;

    Optional<Order> getOrderById(String orderID);

    List<OrderCustomResponse> getOrderByBrandID(String jwtToken, String brandID) throws Exception;

    List<OrderCustomResponse> getOrderByUserID(String jwtToken, String userID) throws Exception;

    List<OrderResponse> getSubOrderByParentID(String parentOrderID);

    List<OrderResponse> getAllOrder();

    OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception;

    void updateOrder(Order order) throws Exception;

    OrderResponse brandPickOrder(OrderPickingRequest orderPickingRequest) throws Exception;

    Order getOrderByDetailID(String detailID);

    Boolean isOrderCompletelyPicked(String orderID);

    Boolean isOrderExpireTime(String orderID);

    List<OrderResponse> getAllParentOrder();

    List<String> filterBrandForSpecificOrderBaseOnDesign(String designID);

    void confirmOrder(String orderID);

    List<OrderStageResponse> getOrderStageByOrderID(String orderID);

    void ratingOrder(String jwtToken, RatingOrderRequest ratingOrderRequest);

    OrderTimeLineResponse getOrderTimeLineByParentOrderID(String parentOrderID);

    OrderTimeLineResponse getOrderTimeLineBySubOrderID(String subOrderID);

    List<FullOrderResponse> getFullProp() throws JsonProcessingException;

    List<FullOrderResponse> getFullPropByBrandID(String brandID) throws JsonProcessingException;

    OrderDetailShippingResponse getOrderDetailShippingResponseByLabelID(String labelID);

    Optional<Order> getParentOrderByOrderIDAndUserID(String orderID, String userID);

    Optional<Order> getSubOrderByOrderIDAndBrandID(String orderID, String brandID);

    OrderStatusDetailResponse getAllOrderStatusDetailResponse();

    GrowthPercentageResponse calculateOrderGrowthPercentageForCurrentAndPreviousMonth();

    List<Pair<Object, Integer>> getAllTotalOrderOfEachBrand();

    SubOrderInvoice getSubOrderInvoiceBySubOrderID(String subOrderID) throws Exception;
}
