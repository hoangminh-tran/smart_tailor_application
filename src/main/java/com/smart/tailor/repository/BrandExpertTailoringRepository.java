package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandExpertTailoring;
import com.smart.tailor.entities.BrandExpertTailoringKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BrandExpertTailoringRepository extends JpaRepository<BrandExpertTailoring, BrandExpertTailoringKey> {
    @Query(value = "select * from brand_expert_tailoring where brand_id = ?1", nativeQuery = true)
    List<BrandExpertTailoring> getBrandExpertTailoringByBrandID(String brandID);

    BrandExpertTailoring getBrandExpertTailoringByBrandExpertTailoringKey_BrandIDAndBrandExpertTailoringKey_ExpertTailoringID(String brandID, String expectTailoringID);

    @Modifying
    @Transactional
    @Query(value = "insert into brand_expert_tailoring (brand_id, expert_tailoring_id, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
    void createShortBrandExpertTailoring(String brandID, String expertTailoringID);
}
