package com.smart.tailor.service;


import com.smart.tailor.utils.request.BrandExpertTailoringRequest;
import com.smart.tailor.utils.response.ExpertTailoringResponse;

import java.util.List;

public interface BrandExpertTailoringService {
    void createExpertTailoringForBrand(String jwtToken, BrandExpertTailoringRequest brandExpertTailoringRequest);

    List<ExpertTailoringResponse> getAllBrandExpertTailoringByBrandID(String jwtToken, String brandID);
}
