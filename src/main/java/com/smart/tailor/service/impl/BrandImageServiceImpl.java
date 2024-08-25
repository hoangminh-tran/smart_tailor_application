package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.repository.BrandImageRepository;
import com.smart.tailor.service.BrandImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BrandImageServiceImpl implements BrandImageService {

    private final BrandImageRepository brandImageRepository;

    @Override
    public BrandImage saveBrandImage(BrandImage brandImage) {
        return brandImageRepository.save(brandImage);
    }

    @Override
    public List<BrandImage> saveAllBrandImages(List<BrandImage> brandImages) {
        return brandImageRepository.saveAll(brandImages);
    }

    @Override
    public Optional<BrandImage> getBrandImageById(String imageId) {
        return brandImageRepository.findById(imageId);
    }

    @Override
    public void deleteBrandImageById(String imageId) {
        brandImageRepository.deleteById(imageId);
    }

    @Override
    public List<BrandImage> getBrandImagesByBrand(Brand brand) {
        return brandImageRepository.findByBrand(brand);
    }

    @Override
    public void updateBrandImageStatusById(String imageId) {
        var image = brandImageRepository.findById(imageId)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Image with ImageID: " + imageId));
        image.setStatus(!image.getStatus());
        brandImageRepository.save(image);
    }
}

