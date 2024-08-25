package com.smart.tailor.service;

import com.smart.tailor.entities.Category;
import com.smart.tailor.utils.request.CategoryListRequest;
import com.smart.tailor.utils.request.CategoryRequest;
import com.smart.tailor.utils.response.CategoryResponse;

import java.util.List;
import java.util.Optional;


public interface CategoryService {
    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndStatus(String categoryName, Boolean status);

    void createCategory(CategoryListRequest categoryListRequest);

    List<CategoryResponse> findAllCatgories();

    CategoryResponse findCategoryByID(String categoryID);

    void updateCategory(CategoryRequest categoryRequest);

    Optional<Category> findCategoryOptionalByID(String categoryID);

    void changeStatusCategory(String categoryID);
}
