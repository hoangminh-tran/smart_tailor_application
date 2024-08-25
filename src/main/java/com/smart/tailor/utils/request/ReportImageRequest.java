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
public class ReportImageRequest {
    @NotBlank(message = "Report Image Name is required")
    @Size(max = 50, message = "Report Image Name must not exceed 50 characters")
    private String reportImageName;

    @NotBlank(message = "Report Image URL is required")
    private String reportImageUrl;
}
