package com.smart.tailor.repository;

import com.smart.tailor.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryID(String categoryID);

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndStatus(String categoryName, Boolean status);
}
