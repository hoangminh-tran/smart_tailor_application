package com.smart.tailor.mapper;

import com.smart.tailor.entities.OrderStage;
import com.smart.tailor.utils.response.OrderStageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderStageMapper {
    @Mapping(source = "orderStage.stageId", target = "stageId")
    @Mapping(source = "orderStage.order.orderID", target = "orderID")
    @Mapping(source = "orderStage.stage", target = "stage")
    @Mapping(source = "orderStage.currentQuantity", target = "currentQuantity")
    @Mapping(source = "orderStage.remainingQuantity", target = "remainingQuantity")
    @Mapping(source = "orderStage.status", target = "status")
    @Mapping(source = "orderStage.createDate", target = "createdDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "orderStage.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    OrderStageResponse mapToOrderStageResponse(OrderStage orderStage);
}
