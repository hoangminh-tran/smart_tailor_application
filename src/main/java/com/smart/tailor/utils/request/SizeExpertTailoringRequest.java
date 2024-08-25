package com.smart.tailor.utils.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SizeExpertTailoringRequest {
    @NotBlank(message = "Expert Tailoring Name is required")
    @Size(max = 50, message = "Expert Tailoring Name must not exceed 50 characters")
    private String expertTailoringName;

    @NotBlank(message = "Size Name is required")
    @Size(max = 5, message = "Size Name must not exceed 5 characters")
    private String sizeName;

    @NotNull(message = "Ratio is required")
    @DecimalMin(value = "0.01", message = "Ratio must be greater than 0")
    private Double ratio;
}
