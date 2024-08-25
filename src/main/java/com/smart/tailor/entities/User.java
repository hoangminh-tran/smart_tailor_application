package com.smart.tailor.entities;


import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;



@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User  implements Serializable, OAuth2User, UserDetails {
    @Id
    @Column(name = "user_id", columnDefinition = "varchar(14)")
    private String userID;

    @Column(columnDefinition = "varchar(255)")
    private String email;

    @Column(columnDefinition = "varchar(255)")
    private String password;

    @Column(columnDefinition = "varchar(50) CHARACTER SET utf8 COLLATE utf8_bin")
    private String fullName;

    @Column(columnDefinition = "varchar(50)")
    private String language;

    @Column(columnDefinition = "varchar(12)")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Roles roles;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Lob
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @OneToOne(mappedBy = "user")
    private Brand brand;

    @OneToOne(mappedBy = "user")
    private Customer customer;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "datetime(2)", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", columnDefinition = "datetime(2)", nullable = true, insertable = false)
    private LocalDateTime lastModifiedDate;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return fullName;
    }

    @PrePersist
    private void prePersist() {
        if (this.userID == null) {
            this.userID = Utilities.generateCustomPrimaryKey();
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
