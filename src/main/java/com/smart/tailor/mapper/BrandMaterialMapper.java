package com.smart.tailor.mapper;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMaterialMapper {
    @Mapping(source = "brandMaterial.material.materialName", target = "materialName")
    @Mapping(source = "brandMaterial.material.category.categoryName", target = "categoryName")
    @Mapping(source = "brandMaterial.brand.brandID", target = "brandID")
    @Mapping(source = "brandMaterial.material.hsCode", target = "hsCode")
    @Mapping(source = "brandMaterial.material.unit", target = "unit")
    @Mapping(source = "brandMaterial.material.basePrice", target = "basePrice")
    @Mapping(source = "brandMaterial.brandPrice", target = "brandPrice")
    @Mapping(source = "brandMaterial.material.materialID", target = "materialID")
    @Mapping(source = "brandMaterial.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "brandMaterial.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    BrandMaterialResponse mapperToBrandMaterialResponse(BrandMaterial brandMaterial);
}