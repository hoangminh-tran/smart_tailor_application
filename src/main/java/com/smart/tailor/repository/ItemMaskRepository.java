package com.smart.tailor.repository;

import com.smart.tailor.entities.ItemMask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemMaskRepository extends JpaRepository<ItemMask, String> {
    @Query(nativeQuery = true, value = "select im.* from item_mask im where im.part_of_design_id = ?1")
    List<ItemMask> getAllItemMaskByPartOfDesignID(String partOfDesignID);
}

