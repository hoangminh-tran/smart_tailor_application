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
@Table(name = "size_expert_tailoring")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeExpertTailoring  implements Serializable {
    @EmbeddedId
    private SizeExpertTailoringKey sizeExpertTailoringKey;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id", nullable = false, insertable = false, updatable = false)
    private ExpertTailoring expertTailoring;

    @ManyToOne
    @JoinColumn(name = "size_id", referencedColumnName = "size_id", nullable = false, insertable = false, updatable = false)
    private Size size;

    private Double ratio;

    private Boolean status;

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
