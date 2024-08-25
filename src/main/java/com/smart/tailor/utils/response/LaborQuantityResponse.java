package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborQuantityResponse {
    private String laborQuantityID;

    private Integer laborQuantityMinQuantity;

    private Integer laborQuantityMaxQuantity;

    private Integer laborQuantityMinPrice;

    private Integer laborQuantityMaxPrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
