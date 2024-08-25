package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailPriceResponse {
    private String totalPriceOfParentOrder;

    private String customerCommissionFee;

    private String customerPriceDeposit;

    private String customerPriceFirstStage;

    private String customerSecondStage;

    private String customerShippingFee;

    private List<BrandDetailPriceResponse> brandDetailPriceResponseList;

    private List<DesignMaterialDetailResponse> designMaterialDetailResponseList;
}
