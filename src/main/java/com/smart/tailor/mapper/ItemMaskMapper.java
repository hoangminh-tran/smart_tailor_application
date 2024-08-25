package com.smart.tailor.mapper;

import com.smart.tailor.entities.ItemMask;
import com.smart.tailor.utils.response.ItemMaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MaterialMapper.class})
public interface ItemMaskMapper {

    @Mapping(source = "itemMask.itemMaskID", target = "itemMaskID")
    @Mapping(source = "itemMask.indexZ", target = "indexZ")
    @Mapping(source = "itemMask.rotate", target = "rotate")
    @Mapping(source = "itemMask.status", target = "status")
    @Mapping(source = "itemMask.topLeftRadius", target = "topLeftRadius")
    @Mapping(source = "itemMask.topRightRadius", target = "topRightRadius")
    @Mapping(source = "itemMask.bottomLeftRadius", target = "bottomLeftRadius")
    @Mapping(source = "itemMask.bottomRightRadius", target = "bottomRightRadius")
    @Mapping(source = "itemMask.material", target = "material")
    @Mapping(source = "itemMask.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "itemMask.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "imageUrl", expression = "java(decodeByteArrayToString(itemMask.getImageUrl()))")
    ItemMaskResponse mapperToItemMaskResponse(ItemMask itemMask);

    default String decodeByteArrayToString(byte[] values) {
        if (values != null) {
            return new String(Base64.getDecoder().decode(values));
        }
        return null;
    }
}
