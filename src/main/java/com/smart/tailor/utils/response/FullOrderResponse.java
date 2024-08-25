package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullOrderResponse {
    private DesignResponse designResponse;
    private String orderID;
    private Boolean paymentStatus;
    private Integer quantity;
    private Float rating;
    private OrderStatus orderStatus;
    private String orderType;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String labelID;
    private String phone;
    private String buyerName;
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
    private List<OrderResponse> subOrderList;
    private List<PaymentResponse> paymentList;
}
