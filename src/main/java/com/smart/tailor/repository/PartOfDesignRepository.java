package com.smart.tailor.repository;

import com.smart.tailor.entities.PartOfDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartOfDesignRepository extends JpaRepository<PartOfDesign, String> {
    @Query(nativeQuery = true, value = "select p.* from part_of_design p where " +
            "p.design_id = ?1 and p.part_of_design_name = ?2")
    Optional<PartOfDesign> getPartOfDesignByDesignIDAndPartOfDesignName(String designID, String partOfDesignName);
}
