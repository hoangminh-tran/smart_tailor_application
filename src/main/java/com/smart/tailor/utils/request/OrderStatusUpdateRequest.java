package com.smart.tailor.utils.request;

import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.validate.ValidEnumValue;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    @NotBlank(message = "Order ID is required")
    private String orderID;

    @NotBlank(message = "Status is required")
    @ValidEnumValue(name = "status", enumClass = OrderStatus.class)
    private String status;
}
