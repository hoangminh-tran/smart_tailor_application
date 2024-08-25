package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDetailPriceResponse {
    private String brandID;

    private String subOrderID;

    private String brandPriceDeposit;

    private String brandPriceFirstStage;

    private String brandPriceSecondStage;
}
