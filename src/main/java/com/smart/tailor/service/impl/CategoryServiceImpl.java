package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Category;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.MultipleErrorException;
import com.smart.tailor.mapper.CategoryMapper;
import com.smart.tailor.repository.CategoryRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.CategoryListRequest;
import com.smart.tailor.utils.request.CategoryRequest;
import com.smart.tailor.utils.response.CategoryResponse;
import com.smart.tailor.utils.response.ErrorDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryMapper categoryMapper;

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public Optional<Category> findByCategoryNameAndStatus(String categoryName, Boolean status) {
        return categoryRepository.findByCategoryNameAndStatus(categoryName, status);
    }

    @Override
    public void createCategory(CategoryListRequest categoryListRequest) {
        var categoryNames = categoryListRequest.getCategoryNames();
        List<ErrorDetail> errorDetails = new ArrayList<>();
        for (int i = 0; i < categoryNames.size(); i++) {
            String categoryName = categoryNames.get(i);
            List<String> errors = new ArrayList<>();

            if (!Utilities.isStringNotNullOrEmpty(categoryName)) {
                errors.add("Category Name is null or empty");
            }

            if (categoryName != null) {
                if (categoryName.length() > 50) {
                    errors.add("Category Name must not exceed 50 characters");
                }

                Optional<Category> categoryOptional = findByCategoryName(categoryName);
                if (categoryOptional.isPresent()) {
                    errors.add("Category Name is existed: " + categoryName);
                }
            }

            if (!errors.isEmpty()) {
                errorDetails.add(new ErrorDetail(categoryName, errors));
            } else {
                categoryRepository.save(
                        Category
                                .builder()
                                .categoryName(categoryName)
                                .status(true)
                                .build()
                );
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST, "Error occur When Create Category", errorDetails);
        }
    }

    @Override
    public List<CategoryResponse> findAllCatgories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::mapperToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse findCategoryByID(String categoryID) {
        var categoryOptional = categoryRepository.findByCategoryID(categoryID);
        if (categoryOptional.isPresent()) {
            return categoryMapper.mapperToCategoryResponse(categoryOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public void updateCategory(CategoryRequest categoryRequest) {
        // Check Category ID is Existed or not
        var categoryResponse = categoryRepository.findByCategoryID(categoryRequest.getCategoryID())
                .orElseThrow(() -> new ItemNotFoundException("Can Not Find Category with CategoryID: " + categoryRequest.getCategoryID()));

        // Check Category Name is Existed or not
        var categoryNameExisted = findByCategoryName(categoryRequest.getCategoryName());

        if (categoryNameExisted.isPresent()) {
            if (!categoryNameExisted.get().getCategoryID().toString().equals((categoryResponse.getCategoryID().toString()))) {
                throw new ItemAlreadyExistException("Category Name is existed: " + categoryRequest.getCategoryName());
            }
        }

        // Update Category when CategoryID is Existed and CategoryName is not Existed
        categoryRepository.save(
                Category
                        .builder()
                        .categoryID(categoryResponse.getCategoryID())
                        .categoryName(categoryRequest.getCategoryName())
                        .status(categoryResponse.getStatus())
                        .build()
        );
    }

    @Override
    public Optional<Category> findCategoryOptionalByID(String categoryID) {
        return categoryRepository.findById(categoryID);
    }

    @Transactional
    @Override
    public void changeStatusCategory(String categoryID) {
        // Check Category ID is Existed or not
        var categoryResponse = categoryRepository.findByCategoryID(categoryID)
                .orElseThrow(() -> new ItemNotFoundException("Can Not Find Category with CategoryID: " + categoryID));

        categoryResponse.setStatus(!categoryResponse.getStatus());
        categoryRepository.save(categoryResponse);
    }
}
