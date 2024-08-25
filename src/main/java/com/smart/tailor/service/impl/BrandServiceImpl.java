package com.smart.tailor.service.impl;

import com.smart.tailor.constant.ErrorConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.enums.BrandStatus;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.BrandMapper;
import com.smart.tailor.repository.BrandRepository;
import com.smart.tailor.service.BrandImageService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandImageRequest;
import com.smart.tailor.utils.request.BrandRequest;
import com.smart.tailor.utils.response.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final UserService userService;
    private final BrandImageService brandImageService;
    private final BrandMapper brandMapper;
    private final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Override
    public Optional<Brand> getBrandById(String brandId) {
        if (brandId == null || brandId.toString().isEmpty()) {
            throw new IllegalArgumentException(MessageConstant.MISSING_ARGUMENT);
        }
        return brandRepository.findById(brandId)
                .or(() -> {
                    var user = userService.getUserByUserID(brandId);
                    return user.isPresent()
                            ? Optional.of(Brand.builder().brandID(brandId).brandStatus(BrandStatus.PENDING).build())
                            : Optional.empty();
                });
    }

    @Override
    public Brand saveBrand(String brandID, BrandRequest brandRequest) {
        var user = userService.getUserByUserID(brandID)
                .orElseThrow(() -> new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new BadRequestException(ErrorConstant.ACCOUNT_NOT_VERIFIED.getMessage());
        }

        Brand savedBrand = brandRepository.save(
                Brand.builder()
                        .user(user)
                        .brandName(brandRequest.getBrandName())
                        .bankName(brandRequest.getBankName())
                        .accountNumber(brandRequest.getAccountNumber())
                        .accountName(brandRequest.getAccountName())
                        .brandStatus(BrandStatus.PENDING)
                        .address(brandRequest.getAddress())
                        .province(brandRequest.getProvince())
                        .ward(brandRequest.getWard())
                        .district(brandRequest.getDistrict())
                        .QR_Payment(brandRequest.getQrPayment())
                        .rating(1.0f)
                        .numberOfRatings(1)
                        .totalRatingScore(1.0f)
                        .numberOfViolations(0)
                        .taxCode(brandRequest.getTaxCode())
                        .build()
        );

        // Save images
        if (brandRequest.getBrandImages() != null) {
            for (BrandImageRequest imageRequest : brandRequest.getBrandImages()) {
                byte[] base64ImageUrl = null;
                if (Optional.ofNullable(imageRequest.getImageUrl()).isPresent()) {
                    base64ImageUrl = Utilities.encodeStringToBase64(imageRequest.getImageUrl());
                }
                BrandImage brandImage = BrandImage.builder()
                        .brand(savedBrand)
                        .imageUrl(base64ImageUrl)
                        .imageDescription(imageRequest.getImageDescription())
                        .status(false)
                        .build();
                brandImageService.saveBrandImage(brandImage);
            }
        }
        return savedBrand;
    }

    @Override
    public Brand getBrandByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException(MessageConstant.MISSING_ARGUMENT);
        }
        return brandRepository.findBrandByUserEmail(email);
    }

    @Override
    public Brand updateBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Optional<Brand> findBrandById(String brandID) {
        return brandRepository.findById(brandID);
    }

    @Override
    public List<Brand> findAllBrandByExpertTailoringID(String expertTailoringID) {
        return brandRepository.findAllBrandByExpertTailoringID(expertTailoringID);
    }

    @Override
    public void ratingBrand(String brandID, Float ratingScore, Float previousScoreRating) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        var numberOfRatingsUpdate = brand.getNumberOfRatings() + 1;
        var totalRatingScoreUpdate = brand.getTotalRatingScore() + ratingScore;
        if(previousScoreRating != null){
            numberOfRatingsUpdate -= 1;
            totalRatingScoreUpdate -= previousScoreRating;
        }
        if(totalRatingScoreUpdate <= 0) totalRatingScoreUpdate = 0;
        var ratingUpdate = totalRatingScoreUpdate / numberOfRatingsUpdate;
        ratingUpdate = Math.max(0, Math.min(ratingUpdate, 5));

        brandRepository.updateBrandRatingAndScore(
                ratingUpdate,
                numberOfRatingsUpdate,
                totalRatingScoreUpdate,
                brandID
        );
    }

    @Override
    public BrandResponse findBrandInformationByBrandID(String brandID) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        return brandMapper.mapperToBrandResponse(brand);
    }

    @Override
    public List<BrandResponse> getAllBrandInformation() {
        return brandRepository
                .findAll()
                .stream()
                .map(brandMapper::mapperToBrandResponse)
                .toList();
    }

    @Override
    public List<BrandImage> getBrandImage(String brandID) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        return brandImageService.getBrandImagesByBrand(brand);
    }

    @Override
    public boolean changeBrandImageStatus(String imageId) {
        try {
            brandImageService.updateBrandImageStatusById(imageId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void ratingBrandCancelOrder(String brandID, Float ratingScore) {
        var brand = findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Brand with BrandID: " + brandID));
        var numberOfRatingsUpdate = brand.getNumberOfRatings() + 1;
        var totalRatingScoreUpdate = brand.getTotalRatingScore() - ratingScore;

        if(totalRatingScoreUpdate <= 0) totalRatingScoreUpdate = 0;
        var ratingUpdate = totalRatingScoreUpdate / numberOfRatingsUpdate;
        ratingUpdate = Math.max(0, Math.min(ratingUpdate, 5));

        brandRepository.updateBrandRatingAndScore(
                ratingUpdate,
                numberOfRatingsUpdate,
                totalRatingScoreUpdate,
                brandID
        );
    }
}
