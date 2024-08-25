package com.smart.tailor.utils.request;

import com.smart.tailor.enums.Provider;
import com.smart.tailor.validate.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @ValidEmail
    @NotBlank(message = "Email is required")
    private String email;

    private String password;

    private Provider provider;
}
