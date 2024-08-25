package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStageResponse {
    String stageId;
    String orderID;
    OrderStatus stage;
    Integer currentQuantity;
    Integer remainingQuantity;
    Boolean status;
    String createdDate;
    String lastModifiedDate;
}
