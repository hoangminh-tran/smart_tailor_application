package com.smart.tailor.service;

import com.smart.tailor.utils.request.OrderShippingRequest;
import com.smart.tailor.utils.response.FeeResponse;
import com.smart.tailor.utils.response.OrderDetailShippingResponse;
import com.smart.tailor.utils.response.OrderShippingResponse;

public interface GHTKShippingService {
    OrderShippingResponse createShippingOrder(OrderShippingRequest orderShippingRequest);

    FeeResponse calculateShippingFee(OrderShippingRequest orderShippingRequest);

    OrderDetailShippingResponse getOrderDetailShippingResponseByLabelID(String labelID);
}
