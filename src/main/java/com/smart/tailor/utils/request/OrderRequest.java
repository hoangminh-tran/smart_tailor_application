package com.smart.tailor.utils.request;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String parentOrderID;
    private String designID;
    private Integer quantity;
    private String orderType;
    private OrderStatus orderStatus;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
}
