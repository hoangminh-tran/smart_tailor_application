package com.smart.tailor.utils.request;

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
public class ExpertTailoringRequest {
    @NotBlank(message = "Expert Tailoring Name is required")
    @Size(max = 50, message = "Expert Tailoring Name must not exceed 50 characters")
    private String expertTailoringName;

    @NotBlank(message = "Size Image Url is required")
    private String sizeImageUrl;

    @NotBlank(message = "Model Image Url is required")
    private String modelImageUrl;
}
