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
public class OrderStageRequest {
    String orderID;

    OrderStatus stage;

    Integer currentQuantity;

    boolean status;
}
