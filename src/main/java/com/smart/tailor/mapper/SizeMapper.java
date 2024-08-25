package com.smart.tailor.mapper;

import com.smart.tailor.entities.Size;
import com.smart.tailor.utils.response.SizeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    @Mapping(source = "size.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "size.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    SizeResponse mapperToSizeResponse(Size size);
}
