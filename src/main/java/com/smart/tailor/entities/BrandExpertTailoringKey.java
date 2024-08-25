package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandExpertTailoringKey implements Serializable {
    @Column(name = "expert_tailoring_id", columnDefinition = "varchar(14)")
    private String expertTailoringID;

    @Column(name = "brand_id", columnDefinition = "varchar(14)")
    private String brandID;
}
