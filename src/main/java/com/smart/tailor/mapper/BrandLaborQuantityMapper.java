package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandLaborQuantity;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandLaborQuantityMapper {
    @Mapping(source = "brandLaborQuantity.laborQuantity.laborQuantityID", target = "laborQuantityID")
    @Mapping(source = "brandLaborQuantity.laborQuantity.laborQuantityMinQuantity", target = "laborQuantityMinQuantity")
    @Mapping(source = "brandLaborQuantity.laborQuantity.laborQuantityMaxQuantity", target = "laborQuantityMaxQuantity")
    @Mapping(source = "brandLaborQuantity.laborQuantity.laborQuantityMinPrice", target = "laborQuantityMinPrice")
    @Mapping(source = "brandLaborQuantity.laborQuantity.laborQuantityMaxPrice", target = "laborQuantityMaxPrice")
    @Mapping(source = "brandLaborQuantity.laborQuantity.status", target = "laborQuantityStatus")
    @Mapping(source = "brandLaborQuantity.brandLaborCostPerQuantity", target = "laborCostPerQuantity")
    @Mapping(source = "brandLaborQuantity.status", target = "brandLaborQuantityStatus")
    @Mapping(source = "brandLaborQuantity.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "brandLaborQuantity.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    BrandLaborQuantityResponse mapToLaborQuantityResponse(BrandLaborQuantity brandLaborQuantity);
}
