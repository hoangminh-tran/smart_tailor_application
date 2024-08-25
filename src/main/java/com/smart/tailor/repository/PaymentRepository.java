package com.smart.tailor.repository;

import com.smart.tailor.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByPaymentID(String paymentID);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT DISTINCT p.* FROM payment p  WHERE p.order_id = ?1")
    List<Payment> findAllByOrderID(String orderID);
}
