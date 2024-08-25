package com.smart.tailor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.utils.request.PayOSRequest;
import com.smart.tailor.utils.response.PayOSCreationResponse;
import com.smart.tailor.utils.response.PayOSResponse;

public interface PayOSService {
    PayOSCreationResponse createPaymentLink(PayOSRequest paymentRequest) throws Exception;

    PayOSCreationResponse createBrandPaymentLink(PayOSRequest paymentRequest) throws Exception;

    PayOSCreationResponse createRefundPaymentLink(PayOSRequest paymentRequest) throws Exception;

    public PayOSResponse getPaymentInfo(Integer paymentID) throws JsonProcessingException;

    public PayOSResponse getBrandPaymentInfo(Integer paymentID) throws JsonProcessingException;

    public PayOSResponse getRefundPaymentInfo(Integer paymentID) throws JsonProcessingException;

    void confirmPayment(Integer orderCode) throws JsonProcessingException;
}
