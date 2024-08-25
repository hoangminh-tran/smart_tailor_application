package com.smart.tailor.entities;

import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "verification_token")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationToken implements Serializable {
    @Id
    @Column(name = "verification_token_id", columnDefinition = "varchar(14)")
    private String verificationTokenID;

    @Column(columnDefinition = "varchar(14)")
    private String token;

    private LocalDateTime expirationDateTime;

    @Enumerated(EnumType.STRING)
    private TypeOfVerification typeOfVerification;

    private boolean isEnabled;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.verificationTokenID == null){
            this.verificationTokenID = Utilities.generateCustomPrimaryKey();
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
