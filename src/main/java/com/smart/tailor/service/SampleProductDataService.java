package com.smart.tailor.service;

import com.smart.tailor.utils.request.SampleProductDataRequest;
import com.smart.tailor.utils.response.SampleProductDataResponse;

import java.util.List;


public interface SampleProductDataService {
    void addNewSampleProductData(SampleProductDataRequest sampleProductDataRequest);

    void updateSampleProductData(String sampleModelID, SampleProductDataRequest sampleProductDataRequest);

    SampleProductDataResponse getSampleProductDataByID(String sampleModelID);

    List<SampleProductDataResponse> getSampleProductDataByParentOrderID(String parentOrderID);

    List<SampleProductDataResponse> getSampleProductDataByParentOrderIDAndStageID(String orderID, String stageID);
}
