package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialWithPriceResponse {
    private String materialID;

    private String materialName;

    private String categoryName;

    private Long hsCode;

    private String unit;

    private Integer basePrice;

    private Integer minPrice;

    private Integer maxPrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
