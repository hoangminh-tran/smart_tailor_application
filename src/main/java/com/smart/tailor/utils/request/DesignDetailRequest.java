package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignDetailRequest {
    List<DesignDetailSize> sizeList;
    private String designId;
    private String orderId;
    private Boolean detailStatus;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
}
