package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;


@Entity
@Table(name = "material")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Material  implements Serializable {
    @Id
    @Column(name = "material_id", columnDefinition = "varchar(14)")
    private String materialID;

    @Column(columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String materialName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "hs_code", columnDefinition = "DECIMAL(38, 0)")
    private BigInteger hsCode;

    @Column(columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String unit;

    @Column(name = "base_price")
    private Integer basePrice;

    private Boolean status;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.materialID == null){
            this.materialID = Utilities.generateCustomPrimaryKey();
        }

        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}
