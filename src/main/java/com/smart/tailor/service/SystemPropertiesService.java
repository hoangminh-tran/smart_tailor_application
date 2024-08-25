package com.smart.tailor.service;

import com.smart.tailor.entities.SystemProperties;
import com.smart.tailor.utils.request.SystemPropertiesRequest;
import com.smart.tailor.utils.request.SystemPropertiesUpdateRequest;
import com.smart.tailor.utils.response.SystemPropertiesResponse;

import java.util.List;
import java.util.Optional;


public interface SystemPropertiesService {
    SystemPropertiesResponse addNewSystemProperty(SystemPropertiesRequest systemProperties);

    List<SystemPropertiesResponse> getAllByPropertyType(String propertyType);

    SystemPropertiesResponse getByID(String propertyID);

    SystemPropertiesResponse getByName(String propertyName);

    Optional<SystemProperties> getObjectByID(String propertyID);

    List<SystemPropertiesResponse> updateSystemProperty(List<SystemPropertiesUpdateRequest> requests);

    List<SystemPropertiesResponse> getAll();
}
