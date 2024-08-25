package com.smart.tailor.repository;

import com.smart.tailor.entities.Brand;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BrandRepository extends JpaRepository<Brand, String> {
    Optional<Brand> getBrandByBrandID(String brandID);

    @Modifying
    @Transactional
    @Query(value = "insert into brand (brand_id, brand_name, brand_status, create_date, last_modified_date) values (?1, ?2, ?3, current_timestamp, null)", nativeQuery = true)
    void createShortBrand(String brandID, String brandName, String brandStatus);

    Optional<Brand> findBrandByBrandName(String brandName);

    @Query(value = "select b.* from brand b join users u on b.brand_id = u.user_id where u.email = ?1", nativeQuery = true)
    Brand findBrandByUserEmail(String email);

    @Query(value = "select b.* from brand b join brand_expert_tailoring bet on b.brand_id = bet.brand_id " +
            "join expert_tailoring et on et.expert_tailoring_id = bet.expert_tailoring_id where et.expert_tailoring_id = ?1 ", nativeQuery = true)
    List<Brand> findAllBrandByExpertTailoringID(String expertTailoringID);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Brand b SET b.rating = ?1, b.number_of_ratings = ?2, b.total_rating_score = ?3, b.last_modified_date = current_timestamp WHERE b.brand_id = ?4", nativeQuery = true)
    void updateBrandRatingAndScore(Float rating, Integer numberOfRatings, Float totalRatingScore, String brandID);
}