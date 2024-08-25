package com.smart.tailor.service.impl;

import com.smart.tailor.entities.BrandExpertTailoring;
import com.smart.tailor.entities.BrandExpertTailoringKey;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.MultipleErrorException;
import com.smart.tailor.exception.UnauthorizedAccessException;
import com.smart.tailor.repository.BrandExpertTailoringRepository;
import com.smart.tailor.service.BrandExpertTailoringService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.ExpertTailoringService;
import com.smart.tailor.service.JwtService;
import com.smart.tailor.utils.request.BrandExpertTailoringRequest;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.ExpertTailoringResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BrandExpertTailoringServiceImpl implements BrandExpertTailoringService {
    private final BrandExpertTailoringRepository brandExpertTailoringRepository;
    private final BrandService brandService;
    private final ExpertTailoringService expertTailoringService;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(BrandExpertTailoringServiceImpl.class);

    @Override
    public void createExpertTailoringForBrand(String jwtToken, BrandExpertTailoringRequest brandExpertTailoringRequest){
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);
        var brandID = brandExpertTailoringRequest.getBrandID();

        if(!userID.equals(brandID)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        List<Object> errorDetails = new ArrayList<>();
        var brand = brandService.findBrandById(brandID);
        String errorBrand = "";

        if(brand.isEmpty()){
            errorBrand = "Can not find Brand by BrandID: " + brandID;
        }

        for(var expertTailoringID : brandExpertTailoringRequest.getExpertTailoringIDList()){
            List<String> errors = new ArrayList<>();

            var expertTailoring = expertTailoringService.findExpertTailoringByID(expertTailoringID);
            if (expertTailoring.isEmpty()) {
                errors.add("Can not find Expert Tailoring with Expert Tailoring ID: " + expertTailoringID);
            }

            var brandExpertTailoringExisted = brandExpertTailoringRepository.
                    getBrandExpertTailoringByBrandExpertTailoringKey_BrandIDAndBrandExpertTailoringKey_ExpertTailoringID(brandID, expertTailoringID);

            if(brandExpertTailoringExisted != null){
                errors.add("Brand Expert Tailoring is Existed with Expert Tailoring ID:" + expertTailoringID + " and Brand ID: " + brandID);
            }

            if (!errorBrand.isEmpty()) {
                errors.add(errorBrand);
            }

            if (!errors.isEmpty()) {
                errorDetails.add(new ErrorDetail(null, errors));
            } else {
                BrandExpertTailoringKey brandExpertTailoringKey = BrandExpertTailoringKey
                        .builder()
                        .brandID(brandID)
                        .expertTailoringID(expertTailoringID)
                        .build();

                brandExpertTailoringRepository.save(
                        BrandExpertTailoring
                            .builder()
                            .brandExpertTailoringKey(brandExpertTailoringKey)
                            .brand(brand.get())
                            .expertTailoring(expertTailoring.get())
                            .build()
                );
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST,
                    "The Brand Expert Tailoring have " + (brandExpertTailoringRequest.getExpertTailoringIDList().size() - errorDetails.size()) + " create Success and " + errorDetails.size() + " create Failure",
                    errorDetails);
        }
    }

    @Override
    public List<ExpertTailoringResponse> getAllBrandExpertTailoringByBrandID(String jwtToken, String brandID) {
        var userID = jwtService.extractUserIDFromJwtToken(jwtToken);

        if(!userID.equals(brandID)){
            throw new UnauthorizedAccessException("You are not authorized to access this resource.");
        }

        var brandExpertTailoringList = brandExpertTailoringRepository.getBrandExpertTailoringByBrandID(brandID);
        List<ExpertTailoringResponse> list = new ArrayList<>();
        for(BrandExpertTailoring brandExpertTailoring : brandExpertTailoringList){
            list.add(expertTailoringService.findByExpertTailoringID(brandExpertTailoring.getBrandExpertTailoringKey().getExpertTailoringID()));
        }
        return list;
    }
}
