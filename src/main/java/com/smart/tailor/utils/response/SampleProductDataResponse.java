package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleProductDataResponse {
    OrderStatus stage;
    Boolean status;
    private String sampleModelID;
    private String orderID;
    private String orderStageID;
    private String brandID;
    private String brandName;
    private String description;

    private String imageUrl;

    private String video;

    private String createDate;

    private String lastModifiedDate;
}
