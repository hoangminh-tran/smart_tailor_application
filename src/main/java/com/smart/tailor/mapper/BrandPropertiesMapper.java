package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandProperties;
import com.smart.tailor.utils.response.BrandPropertiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BrandMapper.class, SystemPropertiesMapper.class})
public interface BrandPropertiesMapper {
    @Mapping(source = "brandProperties.brandPropertyID", target = "brandPropertyID")
    @Mapping(source = "brandProperties.brand", target = "brand")
    @Mapping(source = "brandProperties.systemProperties", target = "systemProperty")
    @Mapping(source = "brandProperties.brandPropertyValue", target = "brandPropertyValue")
    @Mapping(source = "brandProperties.brandPropertyStatus", target = "brandPropertyStatus")
    BrandPropertiesResponse mapperToBrandPropertiesResponse(BrandProperties brandProperties);
}
