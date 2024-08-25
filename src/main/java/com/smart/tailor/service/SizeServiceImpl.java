package com.smart.tailor.service;

import com.smart.tailor.entities.Size;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.exception.MultipleErrorException;
import com.smart.tailor.mapper.SizeMapper;
import com.smart.tailor.repository.SizeRepository;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.ListSizeRequest;
import com.smart.tailor.utils.request.SizeRequest;
import com.smart.tailor.utils.response.ErrorDetail;
import com.smart.tailor.utils.response.SizeResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;
    private final Logger logger = LoggerFactory.getLogger(SizeServiceImpl.class);

    @Override
    public void createSize(ListSizeRequest listSizeRequest) {
        List<Object> errorDetails = new ArrayList<>();
        for (SizeRequest sizeRequest : listSizeRequest.getSizeRequestList()) {
            List<String> errors = new ArrayList<>();
            if (!Utilities.isStringNotNullOrEmpty(sizeRequest.getSizeName())) {
                errors.add("Size Name must not be null or blank");
            }

            if (sizeRequest.getSizeName() == null) {
                if (sizeRequest.getSizeName().length() > 5) {
                    errors.add("Size Name must not exceed 5 characters");
                }

                var sizeExisted = sizeRepository.findBySizeName(sizeRequest.getSizeName().toUpperCase());

                if (sizeExisted.isPresent()) {
                    errors.add("Size is existed with Size Name: " + sizeRequest.getSizeName());
                }
            }

            if (!errors.isEmpty()) {
                errorDetails.add(new ErrorDetail(sizeRequest, errors));
            } else {
                sizeRepository.save(
                        Size
                                .builder()
                                .sizeName(sizeRequest.getSizeName().toUpperCase())
                                .status(true)
                                .build()
                );
            }
        }
        if (!errorDetails.isEmpty()) {
            throw new MultipleErrorException(HttpStatus.BAD_REQUEST, "Error occur When Create Size", errorDetails);
        }
    }

    @Override
    public List<SizeResponse> findAllSizeResponse() {
        return sizeRepository
                .findAll()
                .stream()
                .map(sizeMapper::mapperToSizeResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateSize(String sizeID, SizeRequest sizeRequest) {
        var currentSize = sizeRepository.findById(sizeID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find any Size with SizeID: " + sizeID));

        var sizeExisted = sizeRepository.findBySizeName(sizeRequest.getSizeName().toUpperCase());
        if (sizeExisted.isPresent()) {
            if (!sizeExisted.get().getSizeID().toString().equals(currentSize.getSizeID().toString())) {
                throw new ItemAlreadyExistException("Size is existed with SizeName: " + sizeRequest.getSizeName());
            }
        }
        sizeRepository.save(
                Size
                        .builder()
                        .sizeID(currentSize.getSizeID())
                        .sizeName(sizeRequest.getSizeName().toUpperCase())
                        .status(currentSize.getStatus())
                        .build()
        );
    }

    @Override
    public Optional<Size> findBySizeName(String sizeName) {
        return sizeRepository.findBySizeName(sizeName);
    }

    @Override
    public Optional<Size> findByID(String sizeID) {
        return sizeRepository.findById(sizeID);
    }
}
