package com.smart.tailor.repository;

import com.smart.tailor.entities.ExpertTailoringMaterial;
import com.smart.tailor.entities.ExpertTailoringMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExpertTailoringMaterialRepository extends JpaRepository<ExpertTailoringMaterial, ExpertTailoringMaterialKey> {
    Optional<ExpertTailoringMaterial> findByExpertTailoringExpertTailoringIDAndMaterialMaterialID(String expertTailoringID, String materialID);
}
