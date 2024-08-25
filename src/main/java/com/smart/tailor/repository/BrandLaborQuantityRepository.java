package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandLaborQuantity;
import com.smart.tailor.entities.BrandLaborQuantityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface BrandLaborQuantityRepository extends JpaRepository<BrandLaborQuantity, BrandLaborQuantityKey> {
    @Query(value = "select b.* from brand_labor_quantity b join labor_quantity l on b.labor_quantity_id = l.labor_quantity_id\n" +
            "where l.labor_quantity_id = ?1 && b.brand_id = ?2", nativeQuery = true)
    BrandLaborQuantity getBrandLaborQuantitiesByLaborQuantityIDAndBrandID(String laborQuantity, String BrandID);

    @Query(value = "select b.* from brand_labor_quantity b join labor_quantity l on b.labor_quantity_id = l.labor_quantity_id\n" +
            "where b.brand_id = ?1 && l.labor_quantity_min_quantity <= ?2 && l.labor_quantity_max_quantity >= ?2", nativeQuery = true)
    BrandLaborQuantity getLaborQuantityByBrandIDAndBrandQuantity(String brandID, Integer brandQuantity);

    @Query(value = "select MIN(b.brand_labor_cost_per_quantity) from brand_labor_quantity b join labor_quantity l on b.labor_quantity_id = l.labor_quantity_id " +
            "where l.labor_quantity_min_quantity <= ?1 && l.labor_quantity_max_quantity >= ?1", nativeQuery = true)
    Integer getMinBrandLaborQuantityCostByQuantity(Integer quantity);

    @Query(value = "select MAX(b.brand_labor_cost_per_quantity) from brand_labor_quantity b join labor_quantity l on b.labor_quantity_id = l.labor_quantity_id " +
            "where l.labor_quantity_min_quantity <= ?1 && l.labor_quantity_max_quantity >= ?1", nativeQuery = true)
    Integer getMaxBrandLaborQuantityCostByQuantity(Integer quantity);
}
