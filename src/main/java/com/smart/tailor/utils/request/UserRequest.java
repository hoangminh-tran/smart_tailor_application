package com.smart.tailor.utils.request;


import com.smart.tailor.enums.Provider;
import com.smart.tailor.validate.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @ValidEmail
    @NotBlank(message = "Email is required")
    private String email;

    private String password;

    private String language;

    private String phoneNumber;

    private String fullName;

    private String address;

    private String roleName;

    @JsonIgnore
    private Provider provider;

    private String imageUrl;
}
