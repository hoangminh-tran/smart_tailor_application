package com.smart.tailor.mapper;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.response.DesignCustomResponse;
import com.smart.tailor.utils.response.DesignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;


@Mapper(componentModel = "spring", uses = {UserMapper.class, PartOfDesignMapper.class, ExpertTailoringMapper.class})
public interface DesignMapper {
    @Mapping(source = "design.designID", target = "designID")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "expertTailoring", target = "expertTailoring")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(design.getImageUrl()))")
    @Mapping(source = "design.partOfDesignList", target = "partOfDesign")
    @Mapping(source = "design.minWeight", target = "minWeight")
    @Mapping(source = "design.maxWeight", target = "maxWeight")
    @Mapping(source = "design.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "design.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    DesignResponse mapperToDesignResponse(Design design);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }

    @Mapping(source = "design.designID", target = "designID")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "expertTailoring", target = "expertTailoring")
    @Mapping(source = "design.minWeight", target = "minWeight")
    @Mapping(source = "design.maxWeight", target = "maxWeight")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(design.getImageUrl()))")
    @Mapping(source = "design.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "design.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    DesignCustomResponse mapperToDesignCustomResponse(Design design);
}
