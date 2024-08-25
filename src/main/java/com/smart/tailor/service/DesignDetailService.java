package com.smart.tailor.service;

import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignDetailCustomResponse;
import com.smart.tailor.utils.response.DesignDetailResponse;
import com.smart.tailor.utils.response.OrderDetailPriceResponse;

import java.util.List;
import java.util.Optional;


public interface DesignDetailService {
    DesignDetailCustomResponse findAllByOrderID(String orderID);

    DesignDetailResponse findByID(String orderID);

    APIResponse createDesignDetail(String jwtToken, DesignDetailRequest designDetailRequest);

    DesignDetailResponse updateDesignDetail(DesignDetail designDetail);

    DesignDetail updateDetailByID(DesignDetail designDetail);

    Optional<DesignDetail> getDesignDetailObjectByID(String detailID);

    DesignDetail getDetailOfOrderBaseOnBrandID(String orderID, String brandID);

    List<DesignDetail> getDesignDetailBySubOrderID(String subOrderID);

    OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(String parentOrderID) throws Exception;
}
