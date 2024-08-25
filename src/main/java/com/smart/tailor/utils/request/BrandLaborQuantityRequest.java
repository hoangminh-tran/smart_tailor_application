package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandLaborQuantityRequest {
    @NotNull(message = "laborQuantityID is not null")
    @NotBlank(message = "laborQuantityID is not blank")
    private String laborQuantityID;

    @NotNull(message = "brandLaborCostPerQuantity is required")
    @Min(value = 0, message = "brandLaborCostPerQuantity can not less than 0")
    private Integer brandLaborCostPerQuantity;
}
