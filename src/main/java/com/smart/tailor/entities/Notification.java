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
@Table(name = "notification")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification  implements Serializable {
    @Id
    @Column(name = "notification_id", columnDefinition = "varchar(14)")
    private String notificationID;

    @Column(columnDefinition = "varchar(100)")
    private String action;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "user_id", columnDefinition = "varchar(14)")
    private User recipient;

    @Column(name = "target_id", columnDefinition = "varchar(14)")
    private String targetID;

    private Boolean status;

    @Column(columnDefinition = "varchar(255)")
    private String message;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.notificationID == null){
            this.notificationID = Utilities.generateCustomPrimaryKey();
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
