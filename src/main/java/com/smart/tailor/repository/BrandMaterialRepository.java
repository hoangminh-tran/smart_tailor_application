package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;


@Repository
public interface BrandMaterialRepository extends JpaRepository<BrandMaterial, BrandMaterialKey> {
    @Modifying
    @Transactional
    @Query(value = "update brand_material set brand_price = ?1, last_modified_date = current_timestamp where brand_id = ?2 and material_id = ?3", nativeQuery = true)
    void updateBrandMaterial(Integer brandPrice, String brandID, String materialID);

    @Query(
            value = "select bm.* from brand_material bm join brand b on bm.brand_id = b.brand_id " +
                    "join material m on m.material_id = bm.material_id " +
                    "join category c on c.category_id = m.category_id " +
                    "where c.category_name = ?1 && m.material_name = ?2 && b.brand_id = ?3", nativeQuery = true
    )
    BrandMaterial findBrandMaterialByCategoryNameAndMaterialNameAndBrandID(String categoryName, String materialName, String brandID);

    @Query(value = "SELECT MIN(brand_price) FROM brand_material where material_id = ?1", nativeQuery = true)
    Integer getMinPriceByMaterialID(String materialID);

    @Query(value = "SELECT MAX(brand_price) FROM brand_material where material_id =?1", nativeQuery = true)
    Integer getMaxPriceByMaterialID(String materialID);

    @Query(
            value = "select bm.* from brand_material bm join brand b on bm.brand_id = b.brand_id " +
                    "join material m on m.material_id = bm.material_id " +
                    "join category c on c.category_id = m.category_id " +
                    "where c.category_name = ?1 && m.material_name = ?2 && b.brand_name = ?3 and m.hs_code = ?4 && " +
                    "m.unit = ?5 && m.base_price = ?6 && bm.brand_price = ?7", nativeQuery = true
    )
    BrandMaterial findBrandMaterialByCategoryNameAndMaterialNameAndBrandNameAndHsCodeAndPrice(
            String categoryName, String materialName, String brandName, BigInteger hsCode,
            String unit, Integer basePrice, Integer brandPrice);

    @Query(value = "SELECT brand_price FROM brand_material where brand_id = ?1 && material_id = ?2", nativeQuery = true)
    Integer getBrandPriceByBrandIDAndMaterialID(String brandID, String materialID);

    @Query(value = "SELECT * FROM brand_material where brand_id = ?1 && material_id = ?2", nativeQuery = true)
    BrandMaterial getBrandMaterialByBrandIDAndMaterialID(String brandID, String materialID);
}
