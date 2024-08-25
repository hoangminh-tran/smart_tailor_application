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
public class SizeRequest {
    @NotBlank(message = "Size Name is required")
    @Size(max = 5, message = "Size Name must not exceed 5 characters")
    private String sizeName;
}
