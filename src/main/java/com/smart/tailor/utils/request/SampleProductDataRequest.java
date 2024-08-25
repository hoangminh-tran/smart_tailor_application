package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleProductDataRequest {
    @NotBlank(message = "Order Stage ID is required")
    private String orderStageID;

    @NotBlank(message = "Order ID is required")
    private String orderID;

    @NotBlank(message = "Brand ID is required")
    private String brandID;

    private String description;

    private String imageUrl;

    private String video;

    private Boolean status;
}
