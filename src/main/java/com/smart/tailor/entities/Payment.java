package com.smart.tailor.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.smart.tailor.enums.PaymentMethod;
import com.smart.tailor.enums.PaymentType;
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
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment  implements Serializable {
    @Id
    @Column(name = "payment_id", columnDefinition = "varchar(14)")
    private String paymentID;

    @ManyToOne
    @JoinColumn(name = "payment_sender_id", referencedColumnName = "user_id", nullable = true)
    private User paymentSender;

    @Column(name = "payment_sender_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String paymentSenderName;

    @Column(name = "payment_sender_bank_code", columnDefinition = "varchar(50)")
    private String paymentSenderBankCode;

    @Column(name = "payment_sender_bank_number", columnDefinition = "varchar(50)")
    private String paymentSenderBankNumber;

    @ManyToOne
    @JoinColumn(name = "payment_recipient_id", referencedColumnName = "user_id", nullable = true)
    private User paymentRecipient;

    @Column(name = "payment_recipient_name", columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String paymentRecipientName;

    @Column(name = "payment_recipient_bank_code", columnDefinition = "varchar(50)")
    private String paymentRecipientBankCode;

    @Column(name = "payment_recipient_bank_number", columnDefinition = "varchar(50)")
    private String paymentRecipientBankNumber;

    @Column(name = "payment_amount")
    private Integer paymentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true)
    @JsonBackReference
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    private Integer paymentCode;

    @Column(name = "payment_url", columnDefinition = "TEXT")
    private String paymentURl;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.paymentID == null){
            this.paymentID = Utilities.generateCustomPrimaryKey();
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