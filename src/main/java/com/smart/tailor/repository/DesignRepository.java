package com.smart.tailor.repository;

import com.smart.tailor.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface DesignRepository extends JpaRepository<Design, String> {
    @Query(nativeQuery = true, value = "select de.* from design de inner join " +
            "( select distinct d.design_id from design_detail d join orders o on d.order_id = o.order_id where o.parent_order_id = ?1 or o.order_id = ?1 ) " +
            "f on de.design_id = f.design_id")
    Design findByOrderID(String orderID);
}
