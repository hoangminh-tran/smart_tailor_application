package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.utils.response.BrandImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface BrandImageMapper {
    @Mapping(source = "brandImage.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "brandImage.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(brandImage.getImageUrl()))")
    @Mapping(target = "status", source = "brandImage.status")
    BrandImageResponse mapToBrandImageResponse(BrandImage brandImage);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
