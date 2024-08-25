package com.smart.tailor.service;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.response.BrandResponse;

import java.util.List;
import java.util.Optional;


public interface BrandService {
    Optional<Brand> getBrandById(String brandId) throws Exception;

    Brand saveBrand(String brandID, BrandRequest brandRequest) throws Exception;

    Brand getBrandByEmail(String email) throws Exception;

    Brand updateBrand(Brand brand) throws Exception;

    Optional<Brand> findBrandById(String brandID);

    List<Brand> findAllBrandByExpertTailoringID(String expertTailoringID);

    void ratingBrand(String brandID, Float ratingScore, Float previousScoreRating);

    BrandResponse findBrandInformationByBrandID(String brandID);

    List<BrandResponse> getAllBrandInformation();

    List<BrandImage> getBrandImage(String brandID);

    boolean changeBrandImageStatus(String imageId);

    void ratingBrandCancelOrder(String brandID, Float ratingScore);
}
