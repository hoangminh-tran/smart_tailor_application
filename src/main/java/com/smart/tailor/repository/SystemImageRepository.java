package com.smart.tailor.repository;

import com.smart.tailor.entities.SystemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemImageRepository extends JpaRepository<SystemImage, String> {
    List<SystemImage> findAllByImageType(String imageType);

    List<SystemImage> findAllByIsPremium(Boolean isPremium);
}
