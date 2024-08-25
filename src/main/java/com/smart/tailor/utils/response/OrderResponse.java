package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderID;
    private BrandResponse brand;
    private String parentOrderID;
    private Integer quantity;
    private OrderStatus orderStatus;
    private Float rating;
    private String orderType;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
    private String labelID;
    private Integer totalPrice;
    private String employeeID;
    private String expectedStartDate;
    private String expectedProductCompletionDate;
    private String estimatedDeliveryDate;
    private String productionStartDate;
    private String productionCompletionDate;
    private String createDate;
    private String lastModifiedDate;
    private List<DesignDetailResponse> detailList;
    private List<PaymentResponse> paymentList;
}
