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
public class SystemPropertiesUpdateRequest {
    private String propertyID;

    @NotBlank(message = "Property Name is required")
    @Size(max = 50, message = "Property Name must not exceed 50 characters")
    private String propertyName;

    @NotBlank(message = "Property Unit is required")
    @Size(max = 50, message = "Property Unit must not exceed 50 characters")
    private String propertyUnit;

    @Size(max = 255, message = "Property Detail must not exceed 255 characters")
    private String propertyDetail;

    @NotBlank(message = "Property Type is required")
    @Size(max = 50, message = "Property Type must not exceed 50 characters")
    private String propertyType;

    @Size(max = 100, message = "Property Value must not exceed 100 characters")
    private String propertyValue;

    @NotNull(message = "Property Status is required")
    private Boolean propertyStatus;
}
