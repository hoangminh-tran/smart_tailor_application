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
@Table(name = "design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Design  implements Serializable {
    @Id
    @Column(name = "design_id", columnDefinition = "varchar(14)")
    private String designID;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "expert_tailoring_id", referencedColumnName = "expert_tailoring_id")
    private ExpertTailoring expertTailoring;

    @Column(name = "title_design", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String titleDesign;

    @Column(name = "public_status")
    private Boolean publicStatus;

    @Column(name = "min_weight")
    private Float minWeight;

    @Column(name = "max_weight")
    private Float maxWeight;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Column(name = "color", columnDefinition = "varchar(10)")
    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "design")
    private List<PartOfDesign> partOfDesignList;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.designID == null){
            this.designID = Utilities.generateCustomPrimaryKey();
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
