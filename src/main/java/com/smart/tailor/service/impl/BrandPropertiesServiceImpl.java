package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.BrandProperties;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.BrandPropertiesMapper;
import com.smart.tailor.repository.BrandPropertiesRepository;
import com.smart.tailor.service.BrandPropertiesService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.utils.request.BrandPropertiesRequest;
import com.smart.tailor.utils.response.BrandPropertiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BrandPropertiesServiceImpl implements BrandPropertiesService {
    private final SystemPropertiesService systemService;
    private final BrandService brandService;
    private final BrandPropertiesRepository brandPropertiesRepository;
    private final BrandPropertiesMapper brandPropertiesMapper;
    private final JwtService jwtService;

    @Override
    public List<BrandPropertiesResponse> getAllByBrandID(String brandID) {
        try {
            if (brandID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            return brandPropertiesRepository.getAllByBrand_BrandID(brandID).stream().map(brandPropertiesMapper::mapperToBrandPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BrandPropertiesResponse> getAll() {
        try {
            return brandPropertiesRepository.findAll().stream().map(brandPropertiesMapper::mapperToBrandPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public BrandPropertiesResponse getByID(String propertyID) {
        try {
            if (propertyID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            var property = brandPropertiesRepository.findById(propertyID);
            if (property.isEmpty()) {
                return null;
            }
            return brandPropertiesMapper.mapperToBrandPropertiesResponse(property.get());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional
    @Override
    public BrandPropertiesResponse addNew(String jwtToken, BrandPropertiesRequest brandRequest) throws Exception {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(brandRequest.getBrandID())){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        String brandID = brandRequest.getBrandID();

        var brand = brandService.getBrandById(brandID);
        if (brand.isEmpty()) {
            throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND);
        }


        String systemPropertyID = brandRequest.getSystemPropertyID();
        var systemProperty = systemService.getObjectByID(systemPropertyID);
        if (systemProperty.isEmpty()) {
            throw new BadRequestException(MessageConstant.CAN_NOT_FIND_SYSTEM_PROPERTY);
        }

        String brandPropertyValue = brandRequest.getBrandPropertyValue().trim().toUpperCase();
        Boolean brandPropertyStatus = brandRequest.getBrandPropertyStatus() != null ? brandRequest.getBrandPropertyStatus() : true;

        var existedBrandProperties = getByBrandIDAndPropertyID(brandID, systemPropertyID);
        if(existedBrandProperties != null){
            throw new ItemAlreadyExistException("Brand Properties already existed");
        }

        var newBrandProperties = brandPropertiesRepository.save(
                BrandProperties
                        .builder()
                        .systemProperties(systemProperty.get())
                        .brand(brand.get())
                        .brandPropertyValue(brandPropertyValue)
                        .brandPropertyStatus(brandPropertyStatus)
                        .build()
        );
        return brandPropertiesMapper.mapperToBrandPropertiesResponse(newBrandProperties);
    }

    @Override
    public BrandPropertiesResponse getByBrandIDAndPropertyID(String brandID, String propertyID) {
        try {
            if (brandID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            if (propertyID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            var property = brandPropertiesRepository.findByBrand_BrandIDAndSystemPropertiesPropertyID(brandID, propertyID);
            if (property == null) {
                return null;
            }
            return brandPropertiesMapper.mapperToBrandPropertiesResponse(property);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public BrandPropertiesResponse getBrandProductivityByBrandID(String jwtToken, String brandID) {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(brandID)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        var brand = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand by BrandID: " + brandID));

        return brandPropertiesMapper.mapperToBrandPropertiesResponse(
                brandPropertiesRepository.getBrandProductivityByBrandID(brandID)
        );
    }

    @Override
    public BrandPropertiesResponse updateBrandProperties(String jwtToken, BrandPropertiesRequest brandRequest) throws Exception {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!userID.equals(brandRequest.getBrandID())){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        String brandID = brandRequest.getBrandID();

        var brand = brandService.getBrandById(brandID);
        if (brand.isEmpty()) {
            throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND);
        }

        String systemPropertyID = brandRequest.getSystemPropertyID();
        var systemProperty = systemService.getObjectByID(systemPropertyID);
        if (systemProperty.isEmpty()) {
            throw new BadRequestException(MessageConstant.CAN_NOT_FIND_SYSTEM_PROPERTY);
        }

        String brandPropertyValue = brandRequest.getBrandPropertyValue().trim().toUpperCase();
        Boolean brandPropertyStatus = brandRequest.getBrandPropertyStatus() != null ? brandRequest.getBrandPropertyStatus() : true;

        var existedBrandProperties = getByBrandIDAndPropertyID(brandID, systemPropertyID);
        if(existedBrandProperties == null){
            throw new ItemNotFoundException("Can not find any BrandProperties");
        }

        var updateBrandProperties = brandPropertiesRepository.save(
                BrandProperties
                        .builder()
                        .systemProperties(systemProperty.get())
                        .brand(brand.get())
                        .brandPropertyValue(brandPropertyValue)
                        .brandPropertyStatus(brandPropertyStatus)
                        .build()
        );
        return brandPropertiesMapper.mapperToBrandPropertiesResponse(updateBrandProperties);
    }
}
