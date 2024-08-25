package com.smart.tailor.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;



@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Customer  implements Serializable {
    @Id
    @Column(name = "customer_id", columnDefinition = "varchar(14)")
    private String customerID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)")
    private User user;

    private Boolean gender;

    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Column(columnDefinition = "varchar(255)")
    private String address;

    @Column(columnDefinition = "varchar(100)")
    private String province;

    @Column(columnDefinition = "varchar(100)")
    private String district;

    @Column(columnDefinition = "varchar(100)")
    private String ward;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations = 0;

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
