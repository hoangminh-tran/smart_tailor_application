package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report  implements Serializable {
    @Id
    @Column(name = "report_id", columnDefinition = "varchar(14)")
    private String reportID;

    @Column(name = "type_of_report", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String typeOfReport;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true, unique = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true, unique = false)
    private User user;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "report_status")
    private Boolean reportStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "report")
    @JsonManagedReference
    private List<ReportImage> reportImageList;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.reportID == null){
            this.reportID = Utilities.generateCustomPrimaryKey();
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
