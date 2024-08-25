package com.smart.tailor.entities;

import com.smart.tailor.enums.OrderStatus;
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
@Table(name = "order_stage")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class OrderStage  implements Serializable {
    @Id
    @Column(name = "stage_id", columnDefinition = "varchar(14)")
    String stageId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage", nullable = false)
    OrderStatus stage;

    @Column(name = "current_quantity")
    Integer currentQuantity;

    @Column(name = "remaining_quantity")
    Integer remainingQuantity;

    Boolean status;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.stageId == null){
            this.stageId = Utilities.generateCustomPrimaryKey();
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
