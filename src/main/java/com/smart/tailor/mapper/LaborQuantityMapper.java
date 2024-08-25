package com.smart.tailor.mapper;

import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.utils.response.LaborQuantityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LaborQuantityMapper {
    @Mapping(source = "laborQuantity.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "laborQuantity.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    LaborQuantityResponse mapperToLaborQuantityResponse(LaborQuantity laborQuantity);
}
