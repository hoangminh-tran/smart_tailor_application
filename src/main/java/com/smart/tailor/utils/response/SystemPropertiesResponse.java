package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemPropertiesResponse {
    private String propertyID;
    private String propertyName;
    private String propertyUnit;
    private String propertyDetail;
    private String propertyType;
    private String propertyValue;
    private Boolean propertyStatus;
}
