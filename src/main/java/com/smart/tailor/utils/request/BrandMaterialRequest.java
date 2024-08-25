package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
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
public class BrandMaterialRequest {
    @NotBlank(message = "Brand ID is required")
    private String brandID;

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

    @NotNull(message = "Brand Price is required")
    @Min(value = 0, message = "Brand Price must not be less than 0")
    private Integer brandPrice;

    @Override
    public String toString() {
        return "brandID='" + brandID + '\'' +
                ", materialName='" + materialName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", hsCode=" + hsCode +
                ", unit='" + unit + '\'' +
                ", basePrice=" + basePrice +
                ", brandPrice=" + brandPrice;
    }
}
