package com.smart.tailor.service.impl;

import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.MultipleErrorException;
import com.smart.tailor.mapper.LaborQuantityMapper;
import com.smart.tailor.repository.LaborQuantityRepository;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.request.LaborQuantityRequestList;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.LaborQuantityResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaborQuantityServiceImpl implements LaborQuantityService {
    private final LaborQuantityRepository laborQuantityRepository;
    private final LaborQuantityMapper laborQuantityMapper;
    private final Logger logger = LoggerFactory.getLogger(LaborQuantityServiceImpl.class);

    @Override
    public void createLaborQuantity(LaborQuantityRequestList laborQuantityRequestList) {
        List<ErrorDetail> errorDetails = new ArrayList<>();
        var laborQuantityRequests = laborQuantityRequestList.getLaborQuantityRequests();
        Collections.sort(laborQuantityRequests, new Comparator<LaborQuantityRequest>() {
            @Override
            public int compare(LaborQuantityRequest o1, LaborQuantityRequest o2) {
                return o1.getLaborQuantityMaxQuantity().compareTo(o2.getLaborQuantityMaxQuantity());
            }
        });

        List<LaborQuantityRequest> selectedLaborQuantityRequest = new ArrayList<>();
        int prev_r = Integer.MIN_VALUE;
        for (var request : laborQuantityRequests) {
            var curr_l = request.getLaborQuantityMinQuantity();
            var curr_r = request.getLaborQuantityMaxQuantity();
            if (curr_l > prev_r) {
                selectedLaborQuantityRequest.add(request);
                prev_r = curr_r;
            } else {
                errorDetails.add(new ErrorDetail(request, "Labor Quantity is intersect with another Labor Quantity"));
            }
        }

        for (LaborQuantityRequest laborQuantityRequest : selectedLaborQuantityRequest) {
            List<String> errors = new ArrayList<>();
            Integer laborQuantityMinQuantity = laborQuantityRequest.getLaborQuantityMinQuantity();
            Integer laborQuantityMaxQuantity = laborQuantityRequest.getLaborQuantityMaxQuantity();
            Integer laborQuantityMinPrice = laborQuantityRequest.getLaborQuantityMinPrice();
            Integer laborQuantityMaxPrice = laborQuantityRequest.getLaborQuantityMaxPrice();

            if (laborQuantityMinQuantity > laborQuantityMaxQuantity) {
                errors.add("Min Quantity Can Not Greater Than Max Quantity");
            }

            if (laborQuantityMinPrice > laborQuantityMaxPrice) {
                errors.add("Min Price Can Not Greater Than Max Price");
            }

            var laborQuantity = laborQuantityRepository.findByLaborQuantityMinQuantityAndLaborQuantityMaxQuantity(laborQuantityMinQuantity, laborQuantityMaxQuantity);
            if (laborQuantity.isPresent()) {
                errors.add("Labor Quantity is existed with MinQuantity: " + laborQuantityMinQuantity + " and MaxQuantity: " + laborQuantityMaxQuantity);
            }

            if (!errors.isEmpty()) {
                errorDetails.add(new ErrorDetail(laborQuantityRequest, errors));
            } else {
                laborQuantityRepository.save(
                        LaborQuantity
                                .builder()
                                .laborQuantityMinQuantity(laborQuantityMinQuantity)
                                .laborQuantityMaxQuantity(laborQuantityMaxQuantity)
                                .laborQuantityMinPrice(laborQuantityMinPrice)
                                .laborQuantityMaxPrice(laborQuantityMaxPrice)
                                .status(true)
                                .build()
                );
            }
        }

        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST, "Error occur When Create Labor Quantity", errorDetails);
        }
    }

    @Override
    public List<LaborQuantityResponse> findAllLaborQuantity() {
        return laborQuantityRepository
                .findAll()
                .stream()
                .map(laborQuantityMapper::mapperToLaborQuantityResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateLaborQuantity(String laborQuantityID, LaborQuantityRequest laborQuantityRequest) {
        Integer laborQuantityMinQuantity = laborQuantityRequest.getLaborQuantityMinQuantity();
        Integer laborQuantityMaxQuantity = laborQuantityRequest.getLaborQuantityMaxQuantity();
        Integer laborQuantityMinPrice = laborQuantityRequest.getLaborQuantityMinPrice();
        Integer laborQuantityMaxPrice = laborQuantityRequest.getLaborQuantityMaxPrice();

        var currentLaborQuantity = laborQuantityRepository.findById(laborQuantityID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Labor Quantity by LaborQuantityID: " + laborQuantityID));

        if (laborQuantityMinQuantity > laborQuantityMaxQuantity) {
            throw new BadRequestException("Min Quantity Can Not Greater Than Max Quantity");
        }

        if (laborQuantityMinPrice > laborQuantityMaxPrice) {
            throw new BadRequestException("Min Price Can Not Greater Than Max Price");
        }

        var checkValidMaxRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMinQuantity);
        var checkValidMinRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMaxQuantity);
        var laborQuantityExisted = laborQuantityRepository.findByLaborQuantityMinQuantityAndLaborQuantityMaxQuantity(laborQuantityMinQuantity, laborQuantityMaxQuantity);

        if (laborQuantityExisted.isPresent()) {
            if (!laborQuantityExisted.get().getLaborQuantityID().toString().equals(currentLaborQuantity.getLaborQuantityID().toString())) {
                throw new ItemAlreadyExistException("Labor Quantity is existed with MinQuantity: " + laborQuantityMinQuantity + " and MaxQuantity: " + laborQuantityMaxQuantity);
            }
        } else {
            if (!checkValidMaxRangeQuantity.isEmpty() || !checkValidMinRangeQuantity.isEmpty()) {
                throw new BadRequestException("Current Range Labor Quantity Can not intersect with Another Range Labor Quantity");
            }
        }

        laborQuantityRepository.save(
                LaborQuantity
                        .builder()
                        .laborQuantityID(currentLaborQuantity.getLaborQuantityID())
                        .laborQuantityMinQuantity(laborQuantityMinQuantity)
                        .laborQuantityMaxQuantity(laborQuantityMaxQuantity)
                        .laborQuantityMinPrice(laborQuantityMinPrice)
                        .laborQuantityMaxPrice(laborQuantityMaxPrice)
                        .status(currentLaborQuantity.getStatus())
                        .build()
        );
    }

    @Override
    public Optional<LaborQuantity> findByID(String laborQuantityID) {
        return laborQuantityRepository.findById(laborQuantityID);
    }
}
