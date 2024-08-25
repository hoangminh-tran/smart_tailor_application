package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.constraints.DecimalMin;
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
public class RatingOrderRequest {
    @NotBlank(message = "User ID is required")
    private String userID;

    @NotBlank(message = "Parent Order ID is required")
    private String parentOrderID;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be greater than 1")
    private Integer rating;
}
