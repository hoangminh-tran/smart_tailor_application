package com.smart.tailor.utils.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborQuantityRequest {
    @NotNull(message = "laborQuantityMinQuantity is required")
    @Min(value = 0, message = "laborQuantityMinQuantity can not less than 0")
    private Integer laborQuantityMinQuantity;

    @NotNull(message = "laborQuantityMaxQuantity is required")
    @Min(value = 0, message = "laborQuantityMaxQuantity can not less than 0")
    private Integer laborQuantityMaxQuantity;

    @NotNull(message = "laborQuantityMinPrice is required")
    @Min(value = 0, message = "laborQuantityMinPrice can not less than 0")
    private Integer laborQuantityMinPrice;

    @NotNull(message = "laborQuantityMaxPrice is required")
    @Min(value = 0, message = "laborQuantityMaxPrice can not less than 0")
    private Integer laborQuantityMaxPrice;
}
