package com.smart.tailor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandLaborQuantityKey implements Serializable {
    @Column(name = "labor_quantity_id", columnDefinition = "varchar(14)")
    private String laborQuantityID;

    @Column(name = "brand_id", columnDefinition = "varchar(14)")
    private String brandID;
}
