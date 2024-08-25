package com.smart.tailor.repository;

import com.smart.tailor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(nativeQuery = true, value = "SELECT o.* FROM orders o join design_detail d on d.order_id = o.order_id where o.order_type = 'PARENT_ORDER' and d.design_id = ?1")
    List<Order> getParentOrderByDesignID(String designID);

    @Query(nativeQuery = true, value = "select o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE d.design_detail_id = ?1")
    Order getOrderByDetailID(String designID);

    @Query(nativeQuery = true, value = "select distinct o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE d.brand_id = ?1")
    List<Order> getOrderByBrandID(String brandID);

    @Query(nativeQuery = true, value = "SELECT DISTINCT o2.* FROM orders o2 JOIN orders o1 ON o2.order_id = o1.parent_order_id or o2.order_id = o1.order_id JOIN design_detail d ON o1.order_id = d.order_id JOIN design de ON d.design_id = de.design_id WHERE de.user_id = ?1")
    List<Order> getParentOrderByUserID(String userID);

    @Query(nativeQuery = true, value = "select o.* from orders o " +
            "join orders sub on sub.parent_order_id = o.order_id " +
            "join design_detail dd on sub.order_id = dd.order_id " +
            "join design d on dd.design_id = d.design_id " +
            "join users u on u.user_id = d.user_id " +
            "where o.order_id = ?1 and u.user_id = ?2 and o.order_type = 'PARENT_ORDER'")
    Optional<Order> getParentOrderByOrderIDAndUserID(String orderID, String userID);

    @Query(nativeQuery = true, value = "select o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE o.order_id = ?1 and d.brand_id = ?2 and o.order_type = 'SUB_ORDER'")
    Optional<Order> getSubOrderByOrderIDAndBrandID(String orderID, String brandID);

    @Query(nativeQuery = true, value = "select o.* from orders o join orders sub on sub.parent_order_id = o.order_id where sub.order_id = ?1")
    Optional<Order>  getParentOrderBySubOrderID(String subOrderID);

    @Query(nativeQuery = true, value = "SELECT o.* FROM orders o where o.order_type = 'PARENT_ORDER'")
    List<Order> getAllParentOrder();
}
