package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemImageRequest {
    @NotBlank(message = "Image Name is required")
    @Size(max = 50, message = "Image Name must not exceed 50 characters")
    private String imageName;

    @NotBlank(message = "Image URL is required")
    private String imageURL;

    @NotBlank(message = "Image Type is required")
    @Size(max = 50, message = "Image Type must not exceed 50 characters")
    private String imageType;

    @NotNull(message = "Is Premium is required")
    private Boolean isPremium;
}
