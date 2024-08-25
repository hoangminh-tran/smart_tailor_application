package com.smart.tailor.service;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;

import java.util.List;
import java.util.Optional;


public interface BrandImageService {
    BrandImage saveBrandImage(BrandImage brandImage);

    List<BrandImage> saveAllBrandImages(List<BrandImage> brandImages);

    Optional<BrandImage> getBrandImageById(String imageId);

    void deleteBrandImageById(String imageId);

    List<BrandImage> getBrandImagesByBrand(Brand brand);

    void updateBrandImageStatusById(String imageId);
}
