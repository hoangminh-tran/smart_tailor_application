package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BrandPropertiesRepository extends JpaRepository<BrandProperties, String> {
    List<BrandProperties> getAllByBrand_BrandID(String brandID);

    BrandProperties findByBrand_BrandIDAndSystemPropertiesPropertyID(String brandID, String propertyID);

    @Query(
            value = "select bp.* from brand_properties bp join system_properties sp on bp.property_id = sp.property_id where brand_id = ?1 and sp.property_name = 'BRAND_PRODUCTIVITY'",
            nativeQuery = true
    )
    BrandProperties getBrandProductivityByBrandID(String brandID);
}
