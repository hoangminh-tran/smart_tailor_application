package com.smart.tailor.service;

import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.PartOfDesign;
import com.smart.tailor.utils.request.PartOfDesignRequest;
import com.smart.tailor.utils.response.PartOfDesignResponse;

import java.util.List;


public interface PartOfDesignService {
    List<PartOfDesign> createPartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList);

    List<PartOfDesignResponse> getListPartOfDesignByDesignID(String designID);

    List<PartOfDesign> getListPartOfDesignObjectByDesignID(String designID);

    PartOfDesignResponse getPartOfDesignByPartOfDesignID(String partOfDesignID);

    List<PartOfDesignResponse> getAllPartOfDesign();

    List<PartOfDesign> savePartOfDesign(List<PartOfDesign> partOfDesignList);

    List<PartOfDesign> updatePartOfDesign(Design design, List<PartOfDesignRequest> partOfDesignRequestList);

    List<PartOfDesign> createClonePartOfDesign(Design cloneDesign, List<PartOfDesign> basePartOfDesignList);
}
