package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandLaborQuantityResponse {
    private String laborQuantityID;

    private Integer laborQuantityMinQuantity;

    private Integer laborQuantityMaxQuantity;

    private Integer laborQuantityMinPrice;

    private Integer laborQuantityMaxPrice;

    private Boolean laborQuantityStatus;

    private Integer laborCostPerQuantity;

    private Boolean brandLaborQuantityStatus;

    private String createDate;

    private String lastModifiedDate;
}
