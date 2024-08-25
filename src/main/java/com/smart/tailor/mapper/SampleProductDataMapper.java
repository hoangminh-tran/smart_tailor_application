package com.smart.tailor.mapper;

import com.smart.tailor.entities.SampleProductData;
import com.smart.tailor.utils.response.SampleProductDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface SampleProductDataMapper {
    @Mapping(source = "sampleProductData.orderStage.stageId", target = "orderStageID")
    @Mapping(source = "sampleProductData.brand.brandID", target = "brandID")
    @Mapping(source = "sampleProductData.order.orderID", target = "orderID")
    @Mapping(source = "sampleProductData.brand.brandName", target = "brandName")
    @Mapping(source = "sampleProductData.orderStage.stage", target = "stage")
    @Mapping(source = "sampleProductData.sampleModelID", target = "sampleModelID")
    @Mapping(source = "sampleProductData.status", target = "status")
    @Mapping(target = "video", expression = "java(decodeByteArrayToString(sampleProductData.getVideo()))")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(sampleProductData.getImageUrl()))")
    @Mapping(source = "sampleProductData.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "sampleProductData.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    SampleProductDataResponse mapperToSampleProductDataResponse(SampleProductData sampleProductData);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
