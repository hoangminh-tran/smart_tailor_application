package com.smart.tailor.repository;

import com.smart.tailor.entities.ExpertTailoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExpertTailoringRepository extends JpaRepository<ExpertTailoring, String> {
    Optional<ExpertTailoring> findByExpertTailoringID(String expertTailoringID);

    Optional<ExpertTailoring> findByExpertTailoringNameIgnoreCase(String expertTailoringName);

    Optional<ExpertTailoring> findExpertTailoringByExpertTailoringID(String expectTailoringID);
}
