package com.smart.tailor.utils.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequest {
    @NotBlank(message = "Category Name is required")
    @Size(max = 50, message = "Category Name must not exceed 50 characters")
    private String categoryName;

    @NotBlank(message = "Material Name is required")
    @Size(max = 50, message = "Material Name must not exceed 50 characters")
    private String materialName;

    @NotNull(message = "HS Code is required")
    @Min(value = 0, message = "HS Code must not be less than 0")
    private BigInteger hsCode;

    @NotBlank(message = "Unit is required")
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @NotNull(message = "Base Price is required")
    @Min(value = 0, message = "Base Price must not be less than 0")
    private Integer basePrice;

    @Override
    public String toString() {
        return "categoryName='" + categoryName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", hsCode=" + hsCode +
                ", unit='" + unit + '\'' +
                ", basePrice=" + basePrice;
    }
}
