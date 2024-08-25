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
public class MaterialResponse {
    private String materialID;

    private String materialName;

    private String categoryName;

    private BigInteger hsCode;

    private String unit;

    private Integer basePrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
