package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "part_of_design")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "partOfDesignID")
public class PartOfDesign  implements Serializable {
    @Id
    @Column(name = "part_of_design_id", columnDefinition = "varchar(14)")
    private String partOfDesignID;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "design_id", nullable = false, unique = false)
    private Design design;

    @Column(name = "part_of_design_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String partOfDesignName;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Lob
    @Column(name = "success_image_url", columnDefinition = "LONGTEXT")
    private byte[] successImageUrl;

    @Lob
    @Column(name = "real_part_image_url", columnDefinition = "LONGTEXT")
    private byte[] realPartImageUrl;

    private Integer width;

    private Integer height;

    @OneToMany(mappedBy = "partOfDesign")
    @JsonManagedReference
    @Where(clause = "status = true")
    private List<ItemMask> itemMaskList;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    @JsonManagedReference
    private Material material;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.partOfDesignID == null){
            this.partOfDesignID = Utilities.generateCustomPrimaryKey();
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
