package com.smart.tailor.service;

import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.utils.request.ExpertTailoringMaterialListRequest;
import com.smart.tailor.utils.response.ExpertTailoringMaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface ExpertTailoringMaterialService {
    void createExpertTailoringMaterial(ExpertTailoringMaterialListRequest expertTailoringMaterialListRequest);

    void changeStatusExpertTailoringMaterial(String expertTailoring, String materialID);

    Optional<ExpertTailoringMaterial> findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(String expertTailoringID, String materialID);

    List<ExpertTailoringMaterialResponse> findAllExpertTailoringMaterial();

    List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringID(String expertTailoringID);

    List<ExpertTailoringMaterialResponse> findAllActiveExpertTailoringMaterialByExpertTailoringName(String expertTailoringName);

    void generateSampleExpertTailoringMaterial(HttpServletResponse response) throws IOException;

    void createExpertTailoringMaterialByExcelFile(MultipartFile file);
}