package com.smart.tailor.mapper;

import com.smart.tailor.entities.SystemProperties;
import com.smart.tailor.utils.response.SystemPropertiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SystemPropertiesMapper {
    @Mapping(source = "systemProperties.propertyID", target = "propertyID")
    @Mapping(source = "systemProperties.propertyName", target = "propertyName")
    @Mapping(source = "systemProperties.propertyUnit", target = "propertyUnit")
    @Mapping(source = "systemProperties.propertyDetail", target = "propertyDetail")
    @Mapping(source = "systemProperties.propertyType", target = "propertyType")
    @Mapping(source = "systemProperties.propertyValue", target = "propertyValue")
    @Mapping(source = "systemProperties.propertyStatus", target = "propertyStatus")
    SystemPropertiesResponse mapperToSystemPropertiesResponse(SystemProperties systemProperties);
}
