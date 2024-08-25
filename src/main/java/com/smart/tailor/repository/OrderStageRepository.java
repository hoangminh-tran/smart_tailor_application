package com.smart.tailor.repository;

import com.smart.tailor.entities.OrderStage;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderStageRepository extends JpaRepository<OrderStage, String> {
}
