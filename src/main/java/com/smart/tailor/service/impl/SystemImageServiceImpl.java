package com.smart.tailor.service.impl;

import com.smart.tailor.entities.SystemImage;
import com.smart.tailor.mapper.SystemImageMapper;
import com.smart.tailor.repository.SystemImageRepository;
import com.smart.tailor.service.SystemImageService;
import com.smart.tailor.utils.request.SystemImageRequest;
import com.smart.tailor.utils.response.SystemImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SystemImageServiceImpl implements SystemImageService {
    private final SystemImageRepository systemImageRepository;
    private final SystemImageMapper systemImageMapper;

    @Transactional
    @Override
    public SystemImageResponse addNewSystemImage(SystemImageRequest systemImageRequest) {
        SystemImage systemImage = SystemImage
                .builder()
                .imageName(systemImageRequest.getImageName())
                .imageURL(systemImageRequest.getImageURL())
                .imageType(systemImageRequest.getImageType())
                .isPremium(systemImageRequest.getIsPremium())
                .imageStatus(true)
                .build();
        SystemImage savedSystemImage = systemImageRepository.save(systemImage);
        return systemImageMapper.mapperToSystemImageResponse(savedSystemImage);
    }

    @Override
    public List<SystemImageResponse> getAllSystemImage() {
        return systemImageRepository.findAll().stream().map(this::convertToSystemImageResponse).toList();
    }

    @Override
    public List<SystemImageResponse> getAllSystemImageByImageType(String imageType) {
        return systemImageRepository.findAllByImageType(imageType).stream().map(this::convertToSystemImageResponse).toList();
    }

    @Override
    public List<SystemImageResponse> getAllPremiumSystemImage() {
        return systemImageRepository.findAllByIsPremium(true).stream().map(this::convertToSystemImageResponse).toList();
    }

    @Override
    public List<SystemImageResponse> getSystemImageByTypeAndIsPremium(String imageType, Boolean isPremium) {
        return systemImageRepository.findAllByImageType(imageType)
                .stream()
                .filter(systemImage -> {
                    return systemImage.getIsPremium().equals(isPremium);
                })
                .map(this::convertToSystemImageResponse)
                .toList();
    }

    @Override
    public SystemImageResponse getSystemImageById(String systemImageId) {
        return convertToSystemImageResponse(systemImageRepository.findById(systemImageId).isPresent() ? systemImageRepository.findById(systemImageId).get() : null);
    }

    public SystemImageResponse convertToSystemImageResponse(SystemImage systemImage) {
        return systemImageMapper.mapperToSystemImageResponse(systemImage);
    }
}
