package com.smart.tailor.service;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface BrandMaterialService {
    void createBrandMaterial(String jwtToken, BrandMaterialRequest brandMaterialRequest);

    List<BrandMaterialResponse> getAllBrandMaterial();

    List<BrandMaterialResponse> getAllBrandMaterialByBrandID(String jwtToken, String brandID);

    List<BrandMaterialResponse> getAllBrandMaterialByBrandID(String brandID);

    void createBrandMaterialByImportExcelData(String jwtToken, MultipartFile file, String brandID);

    void updateBrandMaterial(String jwtToken, BrandMaterialRequest brandMaterialRequest);

    Integer getMinPriceByMaterialID(String materialID);

    Integer getMaxPriceByMaterialID(String materialID);

    Optional<BrandMaterial> getPriceByID(BrandMaterialKey key);

    Integer getBrandPriceByBrandIDAndMaterialID(String brandID, String materialID);

    BrandMaterialResponse getBrandMaterialResponseByBrandIDAndMaterialID(String brandID, String materialID);
}
