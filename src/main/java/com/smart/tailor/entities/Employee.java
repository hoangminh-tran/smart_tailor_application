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
@Table(name = "employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee  implements Serializable {
    @Id
    @Column(name = "employee_id", columnDefinition = "varchar(14)")
    private String employeeID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "employee_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)")
    private User user;

    @Column(name = "total_task")
    private Integer totalTask = 0;

    @Column(name = "pending_task")
    private Integer pendingTask = 0;

    @Column(name = "success_task")
    private Integer successTask = 0;

    @Column(name = "fail_task")
    private Integer failTask = 0;

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
