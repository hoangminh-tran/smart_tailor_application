package com.smart.tailor.repository;

import com.smart.tailor.entities.LaborQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LaborQuantityRepository extends JpaRepository<LaborQuantity, String> {
    Optional<LaborQuantity> findByLaborQuantityMinQuantityAndLaborQuantityMaxQuantity(Integer minQuantity, Integer maxQuantity);

    @Query(value = "select l.* from labor_quantity l where l.labor_quantity_max_quantity >= ?1 && l.labor_quantity_min_quantity <= ?1", nativeQuery = true)
    List<LaborQuantity> findLaborQuantityByQuantity(Integer quantity);
}
