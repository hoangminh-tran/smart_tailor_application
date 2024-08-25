package com.smart.tailor.entities;

import com.smart.tailor.enums.BrandStatus;
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
@Table(name = "brand")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Brand implements Serializable {
    @Id
    @Column(name = "brand_id", columnDefinition = "varchar(14)")
    private String brandID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brand_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)")
    private User user;

    @Column(name = "brand_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin", unique = false, nullable = false)
    private String brandName;

    @Column(name = "bank_name", columnDefinition = "varchar(100)", unique = false, nullable = true)
    private String bankName;

    @Column(columnDefinition = "varchar(50)")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "varchar(100)")
    private String accountName;

    @Column(name = "qr_payment", columnDefinition = "varchar(255)")
    private String QR_Payment;

    @Column(columnDefinition = "varchar(255)")
    private String address;

    @Column(columnDefinition = "varchar(100)")
    private String province;

    @Column(columnDefinition = "varchar(100)")
    private String district;

    @Column(columnDefinition = "varchar(100)")
    private String ward;

    @Column(name = "brand_status")
    @Enumerated(EnumType.STRING)
    private BrandStatus brandStatus;

    @Column(name = "number_of_violations")
    private Integer numberOfViolations;

    private Float rating;

    private Integer numberOfRatings;

    private Float totalRatingScore;

    private String taxCode;

    @OneToMany(mappedBy = "brand")
    private List<BrandProperties> brandProperties;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrandImage> brandImages;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        this.numberOfViolations = 0;
        this.numberOfRatings = 1;
        this.totalRatingScore = 1.0f;
        this.rating = 1.0f;
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}
