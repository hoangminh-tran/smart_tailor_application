package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignDetailResponse {
    private String designDetailId;
    private Integer quantity;
    private SizeResponse size;
    private Boolean detailStatus;
}
