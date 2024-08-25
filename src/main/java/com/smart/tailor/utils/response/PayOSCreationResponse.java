package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSCreationResponse {
    private String code;
    private String desc;
    private PayOSCreationResponseData data;
    private String signature;
}
