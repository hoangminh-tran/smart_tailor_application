package com.smart.tailor.mapper;

import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.response.PartOfDesignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;


@Mapper(componentModel = "spring", uses = {ItemMaskMapper.class, MaterialMapper.class})
public interface PartOfDesignMapper {
    @Mapping(source = "partOfDesign.partOfDesignID", target = "partOfDesignID")
    @Mapping(source = "partOfDesign.itemMaskList", target = "itemMasks")
    @Mapping(source = "partOfDesign.material", target = "material")
    @Mapping(source = "partOfDesign.width", target = "width")
    @Mapping(source = "partOfDesign.height", target = "height")
    @Mapping(source = "partOfDesign.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "partOfDesign.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "realPartImageUrl", expression = "java(decodeByteArrayToString(partOfDesign.getRealPartImageUrl()))")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(partOfDesign.getImageUrl()))")
    @Mapping(target = "successImageUrl", expression = "java(decodeByteArrayToString(partOfDesign.getSuccessImageUrl()))")
    PartOfDesignResponse mapperToPartOfDesignResponse(PartOfDesign partOfDesign);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
