package com.smart.tailor.service;

import com.smart.tailor.entities.Material;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.MaterialResponse;
import com.smart.tailor.utils.response.MaterialWithPriceResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface MaterialService {
    Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName);

    void createMaterial(MaterialRequest materialRequest);

    List<MaterialResponse> findAllMaterials();

    List<MaterialResponse> findAllActiveMaterials();

    MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName);

    void createMaterialByExcelFile(MultipartFile file);

    List<MaterialResponse> exportCategoryMaterialForBrandByExcel(HttpServletResponse response) throws IOException;

    MaterialResponse findByMaterialID(String materialID);

    void updateMaterial(String materialID, MaterialRequest materialRequest);

    void updateStatusMaterial(String materialID);

    void generateSampleCategoryMaterialByExportExcel(HttpServletResponse response) throws IOException;

    Optional<Material> findByMaterialName(String materialName);

    Optional<Material> findMaterialByID(String materialID);

    List<MaterialResponse> findListMaterialByCategoryID(String categoryID);

    List<MaterialResponse> findListMaterialByCategoryName(String categoryName);

    List<MaterialWithPriceResponse> findAllMaterialByExpertTailoringIDAndCategoryID(String expertTailoringID, String categoryID);

    Boolean isExistedMaterial(MaterialRequest materialRequest);

    List<Material> findMaterialsByExpertTailoringIDAndCategoryName(String expertTailoringID, String categoryName);
}
