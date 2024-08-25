package com.smart.tailor.mapper;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.utils.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BrandImageMapper.class, UserMapper.class})
public interface BrandMapper {
    @Mapping(source = "brand.brandImages", target = "images")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "brand.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "brand.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    BrandResponse mapperToBrandResponse(Brand brand);
}
