package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandPropertiesRequest;
import com.smart.tailor.utils.response.BrandPropertiesResponse;

import java.util.List;


public interface BrandPropertiesService {
    List<BrandPropertiesResponse> getAllByBrandID(String brandID);

    List<BrandPropertiesResponse> getAll();

    BrandPropertiesResponse getByID(String propertyID);

    BrandPropertiesResponse addNew(String jwtToken, BrandPropertiesRequest brandRequest) throws Exception;

    BrandPropertiesResponse getByBrandIDAndPropertyID(String brandID, String propertyID);

    BrandPropertiesResponse getBrandProductivityByBrandID(String jwtToken, String brandID);

    BrandPropertiesResponse updateBrandProperties(String jwtToken, BrandPropertiesRequest brandRequest) throws Exception;
}
