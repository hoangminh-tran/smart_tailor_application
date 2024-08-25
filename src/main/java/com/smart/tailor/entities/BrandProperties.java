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

@Entity
@Table(name = "brand_properties")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BrandProperties  implements Serializable {
    @Id
    @Column(name = "brand_property_id", columnDefinition = "varchar(14)")
    private String brandPropertyID;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id", nullable = false, unique = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "property_id", nullable = false, unique = false)
    private SystemProperties systemProperties;

    @Column(name = "brand_property_value", columnDefinition = "varchar(150)")
    private String brandPropertyValue;

    @Column(name = "brand_property_status")
    private Boolean brandPropertyStatus;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.brandPropertyID == null){
            this.brandPropertyID = Utilities.generateCustomPrimaryKey();
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
