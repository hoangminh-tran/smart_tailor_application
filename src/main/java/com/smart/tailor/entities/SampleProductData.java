package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "sample_product_data")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SampleProductData  implements Serializable {
    @Id
    @Column(name = "sample_model_id", columnDefinition = "varchar(14)")
    private String sampleModelID;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "stage_id", referencedColumnName = "stage_id")
    @JsonBackReference
    private OrderStage orderStage;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "brand_id")
    @JsonBackReference
    private Brand brand;

    @Column(name = "description", columnDefinition = "varchar(255)")
    private String description;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Lob
    @Column(name = "video", columnDefinition = "LONGTEXT")
    private byte[] video;

    private boolean status;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.sampleModelID == null){
            this.sampleModelID = Utilities.generateCustomPrimaryKey();
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
