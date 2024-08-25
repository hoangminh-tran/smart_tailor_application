package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidColor;
import com.smart.tailor.validate.ValidCustomKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignRequest {
    @NotBlank(message = "User ID is required")
    private String userID;

    @NotBlank(message = "Expert Tailoring ID is required")
    private String expertTailoringID;

    @NotBlank(message = "Title Design is required")
    @Size(max = 50, message = "Title Design must not exceed 50 characters")
    private String titleDesign;

    @NotNull(message = "Public Status is required")
    private Boolean publicStatus;

    @NotNull(message = "Min Weight is required")
    @DecimalMin(value = "0.01", message = "Min Weight must be greater than 0")
    private Float minWeight;

    @NotNull(message = "Max Weight is required")
    @DecimalMin(value = "0.01", message = "Max Weight must be greater than 0")
    private Float maxWeight;

    @ValidColor
    private String color;

    private String imageUrl;

    @NotEmpty(message = "Part of Design is required")
    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
