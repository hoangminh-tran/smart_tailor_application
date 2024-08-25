package com.smart.tailor.service.impl;

import com.smart.tailor.entities.BrandLaborQuantity;
import com.smart.tailor.entities.BrandLaborQuantityKey;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.MultipleErrorException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.mapper.BrandLaborQuantityMapper;
import com.smart.tailor.repository.BrandLaborQuantityRepository;
import com.smart.tailor.service.BrandLaborQuantityService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.request.BrandLaborQuantityRequest;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;
import com.smart.tailor.utils.response.ErrorDetail;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandLaborQuantityServiceImpl implements BrandLaborQuantityService {
    private final BrandLaborQuantityRepository brandLaborQuantityRepository;
    private final BrandLaborQuantityMapper brandLaborQuantityMapper;
    private final BrandService brandService;
    private final LaborQuantityService laborQuantityService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(BrandLaborQuantityServiceImpl.class);

    @Override
    public void createBrandLaborQuantity(String jwtToken, BrandLaborQuantityListRequest brandLaborQuantityListRequest) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!brandLaborQuantityListRequest.getBrandID().equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        List<ErrorDetail> errorDetails = new ArrayList<>();
        String brandID = brandLaborQuantityListRequest.getBrandID();
        var brandExisted = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with brandID: " + brandID));

        var brandLaborQuantityRequests = brandLaborQuantityListRequest.getBrandLaborQuantity();

        for (BrandLaborQuantityRequest brandLaborQuantityRequest : brandLaborQuantityRequests) {
            List<String> errors = new ArrayList<>();
            var laborQuantityID = brandLaborQuantityRequest.getLaborQuantityID();
            var laborQuantity = laborQuantityService.findByID(laborQuantityID).orElse(null);
            if (laborQuantity == null) {
                errorDetails.add(new ErrorDetail("Can not find Labor Quantity with LaborQuantityID: " + laborQuantityID));
            } else {
                if (
                        laborQuantity.getLaborQuantityMinPrice() > brandLaborQuantityRequest.getBrandLaborCostPerQuantity() ||
                                laborQuantity.getLaborQuantityMaxPrice() < brandLaborQuantityRequest.getBrandLaborCostPerQuantity()
                ) {
                    errors.add("Brand Labor Cost must be between Min Price and Max Price");
                }

                var brandLaborQuantityExisted = brandLaborQuantityRepository.getBrandLaborQuantitiesByLaborQuantityIDAndBrandID(laborQuantityID, brandID);

                if (brandLaborQuantityExisted != null) {
                    errors.add("Brand Labor Quantity is existed");
                }

                if (!errors.isEmpty()) {
                    errorDetails.add(new ErrorDetail(brandLaborQuantityRequest, errors));
                } else {
                    BrandLaborQuantityKey brandLaborQuantityKey = BrandLaborQuantityKey
                            .builder()
                            .laborQuantityID(laborQuantityID)
                            .brandID(brandID)
                            .build();

                    BrandLaborQuantity brandLaborQuantity = BrandLaborQuantity
                            .builder()
                            .brandLaborQuantityKey(brandLaborQuantityKey)
                            .brand(brandExisted)
                            .laborQuantity(laborQuantity)
                            .brandLaborCostPerQuantity(brandLaborQuantityRequest.getBrandLaborCostPerQuantity())
                            .status(true)
                            .build();

                    brandLaborQuantityRepository.save(brandLaborQuantity);
                }
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST, "Error occur When Create Brand Labor Quantity", errorDetails);
        }
    }

    @Override
    public List<BrandLaborQuantityResponse> findBrandLaborQuantityByBrandID(String jwtToken, String brandID) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!brandID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        return brandLaborQuantityRepository
                .findAll()
                .stream()
                .filter(brandLaborQuantity -> brandLaborQuantity.getBrand().getBrandID().toString().equals(brandID.toString()))
                .map(brandLaborQuantityMapper::mapToLaborQuantityResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateBrandLaborQuantity(String jwtToken, String brandID, BrandLaborQuantityRequest brandLaborQuantityRequest) {
        var userIDFromJwtToken = jwtService.extractUserIDFromJwtToken(jwtToken);
        if(!brandID.equals(userIDFromJwtToken)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }
        var brandExisted = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandID: " + brandID));

        var laborQuantityID = brandLaborQuantityRequest.getLaborQuantityID();
        var laborQuantity = laborQuantityService.findByID(laborQuantityID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find LaborQuantity with LaborQuantityID: " + laborQuantityID));

        if (
                laborQuantity.getLaborQuantityMinPrice() > brandLaborQuantityRequest.getBrandLaborCostPerQuantity() ||
                        laborQuantity.getLaborQuantityMaxPrice() < brandLaborQuantityRequest.getBrandLaborCostPerQuantity()
        ) {
            throw new BadRequestException("Brand Labor Cost must be between Min and Max Price");
        }

        var brandLaborQuantityExisted = brandLaborQuantityRepository.getBrandLaborQuantitiesByLaborQuantityIDAndBrandID(laborQuantityID, brandID);
        if (brandLaborQuantityExisted == null) {
            throw new ItemNotFoundException("Can not find Brand Labor Quantity");
        }

        BrandLaborQuantityKey brandLaborQuantityKey = BrandLaborQuantityKey
                .builder()
                .laborQuantityID(laborQuantityID)
                .brandID(brandID)
                .build();

        BrandLaborQuantity brandLaborQuantity = BrandLaborQuantity
                .builder()
                .brandLaborQuantityKey(brandLaborQuantityKey)
                .brand(brandExisted)
                .laborQuantity(laborQuantity)
                .brandLaborCostPerQuantity(brandLaborQuantityRequest.getBrandLaborCostPerQuantity())
                .status(brandLaborQuantityExisted.getStatus())
                .build();

        brandLaborQuantityRepository.save(brandLaborQuantity);
    }

    @Override
    public BrandLaborQuantityResponse findLaborQuantityByBrandIDAndBrandQuantity(String brandID, Integer brandQuantity) {
        return brandLaborQuantityMapper.mapToLaborQuantityResponse(
                brandLaborQuantityRepository.getLaborQuantityByBrandIDAndBrandQuantity(brandID, brandQuantity)
        );
    }

    @Override
    public Integer getMinBrandLaborQuantityCostByQuantity(Integer quantity) {
        var brandLaborQuantityCost = brandLaborQuantityRepository.getMinBrandLaborQuantityCostByQuantity(quantity);
        if (brandLaborQuantityCost == null){
            return 0;
        }
        return brandLaborQuantityCost;
    }

    @Override
    public Integer getMaxBrandLaborQuantityCostByQuantity(Integer quantity) {
        var brandLaborQuantityCost = brandLaborQuantityRepository.getMaxBrandLaborQuantityCostByQuantity(quantity);
        if (brandLaborQuantityCost == null){
            return 0;
        }
        return brandLaborQuantityCost;
    }
}
