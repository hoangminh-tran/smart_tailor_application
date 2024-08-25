package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignMaterialDetail {
    private MaterialResponse materialResponse;
    private Integer quantity;
    private Integer minPrice;
    private Integer maxPrice;
}
