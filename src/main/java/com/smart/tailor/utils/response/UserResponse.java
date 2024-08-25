package com.smart.tailor.utils.response;


import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userID;

    private String email;

    private String fullName;

    private String language;

    private String phoneNumber;

    private Provider provider;

    private UserStatus userStatus;

    private String roleName;

    private String imageUrl;

    private String createDate;

    private String lastModifiedDate;
}
