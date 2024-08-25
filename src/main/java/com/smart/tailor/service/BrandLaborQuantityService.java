package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.request.BrandLaborQuantityRequest;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;

import java.util.List;


public interface BrandLaborQuantityService {
    void createBrandLaborQuantity(String jwtToken, BrandLaborQuantityListRequest brandLaborQuantityListRequest);

    List<BrandLaborQuantityResponse> findBrandLaborQuantityByBrandID(String jwtToken, String brandID);

    void updateBrandLaborQuantity(String jwtToken, String brandID, BrandLaborQuantityRequest brandLaborQuantityListRequest);

    BrandLaborQuantityResponse findLaborQuantityByBrandIDAndBrandQuantity(String brandID, Integer brandQuantity);

    Integer getMinBrandLaborQuantityCostByQuantity(Integer quantity);

    Integer getMaxBrandLaborQuantityCostByQuantity(Integer quantity);
}
