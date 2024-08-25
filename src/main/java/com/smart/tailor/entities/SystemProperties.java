package com.smart.tailor.entities;

import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "system_properties")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class SystemProperties  implements Serializable {
    @Id
    @Column(name = "property_id", columnDefinition = "varchar(14)")
    private String propertyID;

    @Column(name = "property_name", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String propertyName;

    @Column(name = "property_unit", columnDefinition = "varchar(50)", nullable = false, unique = false)
    private String propertyUnit;

    @Column(name = "property_detail", columnDefinition = "varchar(255)", nullable = true, unique = false)
    private String propertyDetail;

    @Column(name = "property_type", columnDefinition = "varchar(50)", nullable = false, unique = false)
    private String propertyType;

    @Column(name = "property_value", columnDefinition = "varchar(100)", nullable = true, unique = false)
    private String propertyValue;

    @Column(name = "property_status")
    private Boolean propertyStatus;

    @OneToMany(mappedBy = "systemProperties")
    private List<BrandProperties> brandProperties;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.propertyID == null){
            this.propertyID = Utilities.generateCustomPrimaryKey();
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
