package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandMaterialResponse {
    private String brandID;

    private String materialID;

    private String categoryName;

    private String materialName;

    private BigInteger hsCode;

    private String unit;

    private Integer basePrice;

    private Integer brandPrice;

    private String createDate;

    private String lastModifiedDate;
}
