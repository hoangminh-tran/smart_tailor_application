package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.utils.request.CloneDesignRequest;
import com.smart.tailor.utils.request.DesignRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignResponse;

import java.util.List;


public interface DesignService {
    APIResponse createDesign(String jwtToken, DesignRequest designRequest);

    Design getDesignByID(String designID);

    DesignResponse getDesignByOrderID(String orderID);

    Design getDesignObjectByOrderID(String orderID);

    DesignResponse getDesignResponseByID(String designID);

    List<DesignResponse> getAllDesignByUserID(String jwtToken, String userID);

    List<DesignResponse> getAllDesign();

    APIResponse getAllDesignByUserIDAndRoleName(String jwtToken, String userID, String roleName);

    void updatePublicStatusDesign(String jwtToken, String designID);

    Design saveDesign(Design design);

    void addNewCloneDesignFromBrandDesign(String jwtToken, CloneDesignRequest cloneDesignRequest);

    APIResponse updateDesign(String jwtToken, String designID, DesignRequest designRequest);

    Design createCloneDesignFromBaseDesign(String baseDesignID);
}
