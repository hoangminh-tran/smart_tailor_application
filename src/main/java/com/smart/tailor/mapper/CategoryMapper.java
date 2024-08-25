package com.smart.tailor.mapper;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(source = "category.createDate", target = "createDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "category.lastModifiedDate", target = "lastModifiedDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(source = "category.status", target = "status")
    CategoryResponse mapperToCategoryResponse(Category category);

    Category mapperToCategory(CategoryResponse categoryResponse);
}
