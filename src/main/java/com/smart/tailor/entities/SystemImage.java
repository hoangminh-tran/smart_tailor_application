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
@Table(name = "system_image")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class SystemImage  implements Serializable {
    @Id
    @Column(name = "image_id", columnDefinition = "varchar(14)")
    private String imageID;

    @Column(name = "image_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String imageName;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String imageURL;

    @Column(name = "image_status")
    private Boolean imageStatus;

    @Column(name = "image_type", columnDefinition = "varchar(50)")
    private String imageType;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.imageID == null){
            this.imageID = Utilities.generateCustomPrimaryKey();
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
