package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    @ValidEmail
    @NotBlank(message = "Email is required")
    private String email;

    @Size(max = 50, message = "Full name must not exceed 50 characters")
    private String fullName;

    @Size(min = 10, message = "Phone number must not less than 10 characters")
    @Size(max = 12, message = "Phone number must not exceed 12 characters")
    private String phoneNumber;

    private String imageUrl;

    private Boolean gender;

    private String dateOfBirth;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Size(max = 100, message = "Province must not exceed 100 characters")
    private String province;

    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;
}
