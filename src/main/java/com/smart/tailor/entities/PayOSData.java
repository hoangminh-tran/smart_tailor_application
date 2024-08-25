package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "payos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOSData  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payOSID;

    @Column(name = "order_code")
    private Integer orderCode; // Mã đơn hàng từ cửa hàng

    private Integer amount;

    @Column(columnDefinition = "varchar(20)")
    private String status;

    @Column(columnDefinition = "varchar(255)")
    private String checkoutUrl; // Link thanh toán

    @Column(columnDefinition = "varchar(255)")
    private String qrCode; // Mã VietQR dạng text

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}
