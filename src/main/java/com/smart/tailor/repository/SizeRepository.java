package com.smart.tailor.repository;

import com.smart.tailor.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SizeRepository extends JpaRepository<Size, String> {
    Optional<Size> findBySizeName(String sizeName);
}
