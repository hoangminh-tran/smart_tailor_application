package com.smart.tailor.mapper;

import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpertTailoringMaterialMapper {
    @Mapping(source = "expertTailoringMaterial.status", target = "status")
    @Mapping(source = "expertTailoringMaterial.expertTailoringMaterialKey.expertTailoringID", target = "expertTailoringID")
    @Mapping(source = "expertTailoringMaterial.expertTailoringMaterialKey.materialID", target = "materialID")
    @Mapping(source = "expertTailoringMaterial.expertTailoring.expertTailoringName", target = "expertTailoringName")
    @Mapping(source = "expertTailoringMaterial.material.materialName", target = "materialName")
    @Mapping(source = "expertTailoringMaterial.material.category.categoryName", target = "categoryName")
    @Mapping(source = "expertTailoringMaterial.material.category.categoryID", target = "categoryID")
    @Mapping(source = "expertTailoringMaterial.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "expertTailoringMaterial.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    ExpertTailoringMaterialResponse mapperToExpertTailoringMaterialResponse(ExpertTailoringMaterial expertTailoringMaterial);
}
