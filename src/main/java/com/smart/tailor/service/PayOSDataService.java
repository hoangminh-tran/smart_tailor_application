package com.smart.tailor.service;

import com.smart.tailor.entities.PayOSData;
import com.smart.tailor.utils.response.PayOSDataResponse;

import java.util.Optional;

public interface PayOSDataService {
    void save(PayOSData payOSData);

    Optional<PayOSData> findByOrderCode(Integer code);

    PayOSDataResponse getPayOSDataResponseByOrderCode(Integer code);
}
