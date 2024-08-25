package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.smart.tailor.enums.PrintType;
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
@Table(name = "item_mask")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "itemMaskID")
public class ItemMask  implements Serializable {
    @Id
    @Column(name = "item_mask_id", columnDefinition = "varchar(14)")
    private String itemMaskID;

    @ManyToOne
    @JoinColumn(name = "part_of_design_id", referencedColumnName = "part_of_design_id")
    @JsonBackReference
    private PartOfDesign partOfDesign;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "material_id")
    @JsonBackReference
    private Material material;

    @Column(name = "item_mask_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String itemMaskName;

    @Column(name = "type_of_item", columnDefinition = "varchar(50)")
    private String typeOfItem;

    @Column(name = "is_system_item")
    private Boolean isSystemItem;

    @Column(name = "position_x")
    private Float positionX;

    @Column(name = "position_y")
    private Float positionY;

    @Column(name = "scale_x")
    private Float scaleX;

    @Column(name = "scale_y")
    private Float scaleY;

    @Column(name = "index_z")
    private Integer indexZ;

    @Column(name = "rotate")
    private Float rotate;

    @Column(name = "top_left_radius")
    private Float topLeftRadius;

    @Column(name = "top_right_radius")
    private Float topRightRadius;

    @Column(name = "bottom_left_radius")
    private Float bottomLeftRadius;

    @Column(name = "bottom_right_radius")
    private Float bottomRightRadius;

    private Boolean status;

    @Lob
    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private byte[] imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "print_type")
    private PrintType printType;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.itemMaskID == null){
            this.itemMaskID = Utilities.generateCustomPrimaryKey();
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
