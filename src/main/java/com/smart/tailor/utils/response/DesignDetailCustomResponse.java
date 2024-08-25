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
public class DesignDetailCustomResponse {
    DesignResponse design;
    OrderResponse order;
    List<DesignDetailResponse> designDetail;
}
