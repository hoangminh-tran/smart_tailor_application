package com.smart.tailor.mapper;

import com.smart.tailor.entities.SystemImage;
import com.smart.tailor.utils.response.SystemImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SystemImageMapper {
    @Mapping(source = "systemImage.imageID", target = "imageID")
    @Mapping(source = "systemImage.imageName", target = "imageName")
    @Mapping(source = "systemImage.imageURL", target = "imageURL")
    @Mapping(source = "systemImage.imageStatus", target = "imageStatus")
    @Mapping(source = "systemImage.imageType", target = "imageType")
    @Mapping(source = "systemImage.isPremium", target = "isPremium")
    SystemImageResponse mapperToSystemImageResponse(SystemImage systemImage);
}
