package com.smart.tailor.repository;

import com.smart.tailor.entities.SystemProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemPropertiesRepository extends JpaRepository<SystemProperties, String> {
    List<SystemProperties> getAllByPropertyType(String propertyType);

    SystemProperties findByPropertyName(String name);
}
