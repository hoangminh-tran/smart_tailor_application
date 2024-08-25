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
@Table(name = "expert_tailoring")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpertTailoring  implements Serializable {
    @Id
    @Column(name = "expert_tailoring_id", columnDefinition = "varchar(14)")
    private String expertTailoringID;

    @Column(name = "expert_tailoring_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String expertTailoringName;

    @Lob
    @Column(name = "size_image_url", columnDefinition = "LONGTEXT")
    private String sizeImageUrl;

    @Lob
    @Column(name = "model_image_url", columnDefinition = "LONGTEXT")
    private String modelImageUrl;

    private Boolean status;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.expertTailoringID == null){
            this.expertTailoringID = Utilities.generateCustomPrimaryKey();
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
