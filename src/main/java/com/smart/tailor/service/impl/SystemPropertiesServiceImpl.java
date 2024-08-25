package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.SystemProperties;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.SystemPropertiesMapper;
import com.smart.tailor.repository.SystemPropertiesRepository;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.SystemPropertiesRequest;
import com.smart.tailor.utils.request.SystemPropertiesUpdateRequest;
import com.smart.tailor.utils.response.SystemPropertiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SystemPropertiesServiceImpl implements SystemPropertiesService {
    private final SystemPropertiesRepository systemRepository;
    private final SystemPropertiesMapper systemMapper;

    @Transactional
    @Override
    public SystemPropertiesResponse addNewSystemProperty(SystemPropertiesRequest systemProperties) {
        try {
            String propertyName = systemProperties.getPropertyName();
            String propertyUnit = systemProperties.getPropertyUnit();
            String propertyDetail = systemProperties.getPropertyDetail() != null ? systemProperties.getPropertyDetail() : "";
            String propertyType = systemProperties.getPropertyType().trim().toUpperCase();
            String propertyValue = systemProperties.getPropertyValue() != null ? systemProperties.getPropertyValue() : "";
            Boolean propertyStatus = systemProperties.getPropertyStatus() != null ? systemProperties.getPropertyStatus() : true;

            var newProperty = systemRepository.save(SystemProperties.builder().propertyName(propertyName).propertyUnit(propertyUnit).propertyDetail(propertyDetail).propertyType(propertyType).propertyValue(propertyValue).propertyStatus(propertyStatus).build());
            if (newProperty != null) {
                return systemMapper.mapperToSystemPropertiesResponse(newProperty);
            }
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<SystemPropertiesResponse> getAllByPropertyType(String propertyType) {
        try {
            propertyType = propertyType.trim().toUpperCase();
            if (!Utilities.isNonNullOrEmpty(propertyType)) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            return systemRepository.getAllByPropertyType(propertyType).stream().map(systemMapper::mapperToSystemPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public SystemPropertiesResponse getByID(String propertyID) {
        try {
            var property = systemRepository.findById(propertyID);
            if (property == null) {
                return null;
            }
            return systemMapper.mapperToSystemPropertiesResponse(property.get());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public SystemPropertiesResponse getByName(String propertyName) {
        try {
            var property = systemRepository.findByPropertyName(propertyName);
            if (property == null) {
                return null;
            }
            return systemMapper.mapperToSystemPropertiesResponse(property);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Optional<SystemProperties> getObjectByID(String propertyID) {
        return systemRepository.findById(propertyID);
    }

    @Override
    public List<SystemPropertiesResponse> updateSystemProperty(List<SystemPropertiesUpdateRequest> requests) {
        List<SystemPropertiesResponse> responseList = new ArrayList<>();
        try {
            if (requests == null) {
                throw new Exception(MessageConstant.MISSING_ARGUMENT);
            }
            if (requests.isEmpty()) {
                return new ArrayList<>();
            }
            for (var propRequest : requests) {
                var prop = systemRepository.findById(propRequest.getPropertyID()).orElseThrow(() -> {
                    return new ItemNotFoundException(MessageConstant.RESOURCE_NOT_FOUND + " : " + propRequest.getPropertyID());
                });
                prop.setPropertyDetail(propRequest.getPropertyDetail());
                prop.setPropertyName(propRequest.getPropertyName());
                prop.setPropertyUnit(propRequest.getPropertyUnit());
                prop.setPropertyType(propRequest.getPropertyType());
                prop.setPropertyStatus(propRequest.getPropertyStatus());
                prop.setPropertyValue(propRequest.getPropertyValue());
                var newProp = systemRepository.save(prop);

                responseList.add(systemMapper.mapperToSystemPropertiesResponse(newProp));

            }
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
        return responseList;
    }

    @Override
    public List<SystemPropertiesResponse> getAll() {
        try {
            return systemRepository.findAll().stream().map(systemMapper::mapperToSystemPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
