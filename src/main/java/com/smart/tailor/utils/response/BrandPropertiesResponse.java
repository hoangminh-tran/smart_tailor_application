package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandPropertiesResponse {
    private String brandPropertyID;
    private BrandResponse brand;
    private SystemPropertiesResponse systemProperty;
    private String brandPropertyValue;
    private Boolean brandPropertyStatus;
}
