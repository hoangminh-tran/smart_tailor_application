package com.smart.tailor.service;

import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.request.LaborQuantityRequestList;
import com.smart.tailor.utils.response.LaborQuantityResponse;

import java.util.List;
import java.util.Optional;


public interface LaborQuantityService {
    void createLaborQuantity(LaborQuantityRequestList laborQuantityRequestList);

    List<LaborQuantityResponse> findAllLaborQuantity();

    void updateLaborQuantity(String laborQuantityID, LaborQuantityRequest laborQuantityRequest);

    Optional<LaborQuantity> findByID(String laborQuantityID);
}
