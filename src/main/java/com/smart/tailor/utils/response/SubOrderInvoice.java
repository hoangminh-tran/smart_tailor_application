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
public class SubOrderInvoice {
    private List<BrandMaterialResponse> brandMaterialResponseList;
    private OrderCustomResponse orderCustomResponse;
    private Integer brandLaborQuantity;
}
