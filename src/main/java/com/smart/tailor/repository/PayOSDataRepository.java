package com.smart.tailor.repository;

import com.smart.tailor.entities.PayOSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayOSDataRepository extends JpaRepository<PayOSData, Integer> {
    Optional<PayOSData> findByOrderCode(Integer orderCode);
}
